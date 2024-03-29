from django.db import models
from django.utils.timezone import utc
from mission import generate_mission


class Game(models.Model):
    name = models.CharField(max_length=500)
    in_game = models.BooleanField(default=False)
    # Approximate location of game
    longitude = models.FloatField(null=True, blank=True)
    latitude = models.FloatField(null=True, blank=True)

    is_public = models.BooleanField(default=True)

    def start_game(self):
        if self.player_set.all().count() >= 5:
            self.reassign_missions()

    def add_player(self, player):
        player.game = self
        player.save()
        player.clear_game_info()
        if not self.in_game and self.player_set.count() >= 5:
            self.start_game()
    
    def reassign_missions(self):
        mission_arr = generate_mission([p.device_id for p in self.player_set.all().order_by("pk")])
        for x in mission_arr:
            device_id = x.keys()[0]
            player = self.player_set.get(device_id=device_id)
            player.target = x[device_id]['target']
            player.friendlys = ",".join(x[device_id]['friendlys'])
            player.save()




class Player(models.Model):
    username = models.CharField(max_length=100)
    device_id = models.CharField(max_length=2000, unique=True)

    first_name = models.CharField(max_length=100, default="")
    last_name = models.CharField(max_length=100, default="")

    # Current game information
    game = models.ForeignKey(Game, null=True)

    last_longitude = models.FloatField(null=True, blank=True)
    last_latitude = models.FloatField(null=True, blank=True)
    last_altitude = models.FloatField(null=True, blank=True)
    last_seen = models.DateTimeField(null=True, blank=True)

    bombs_remaining = models.IntegerField(default=1)

    hitlist = models.CharField(blank=True, max_length=2000)
    target = models.CharField(blank=True, max_length=2000)
    friendlys = models.CharField(blank=True, max_length=2000)


    class Meta:
        verbose_name = 'Player'
        verbose_name_plural = 'Players'

    def __unicode__(self):
        return self.username

    def clear_game_info(self):
        self.last_longitude = None
        self.last_latitude = None
        self.last_altitude = None
        self.last_seen = None
        hitlist = target = friendlys = ""
        self.bombs_remaining = 1
        self.save()

    def get_last_loc(self):
        return self.last_longitude, self.last_latitude, self.last_altitude

    def place_bomb(self, longitude, latitude, altitude):
        if self.bombs_remaining > 0:
            new_bomb = Bomb(placed_by=self, longitude=longitude, latitude=latitude, altitude=altitude)
            new_bomb.save()
            self.bombs_remaining-=1
            self.save()
            return new_bomb
        else:
            return None

    def killed_target_type(self, victim):
        if victim.device_id == self.target:
            return 0
        elif victim.device_id in self.hitlist.split(","):
            return 1
        elif victim.device_id in self.friendlys.split(","):
            return 2
        return None


class Bomb(models.Model):
    placed_by = models.ForeignKey(Player)
    time_placed = models.DateTimeField(auto_now_add=True)
    active = models.BooleanField(default=True)
    radius = models.IntegerField(default=10)
    
    longitude = models.FloatField(null=True, blank=True)
    latitude = models.FloatField(null=True, blank=True)
    altitude = models.FloatField(null=True, blank=True)

    class Meta:
        verbose_name = 'Bomb'
        verbose_name_plural = 'Bombs'

    def __unicode__(self):
        return "(%s) <%.3f, %.3f, %.3f>" % (self.placed_by.username, self.longitude, self.latitude, self.altitude)

    def get_info(self):
        return {"longitude": self.longitude, "latitude": self.latitude, 
                "altitude": self.altitude, "placed_by": self.placed_by.device_id, 
                "time_placed": self.time_placed.replace(tzinfo=utc).isoformat(),
                "radius": self.radius, "id": self.id}


def bombs_in_radius(longitude, latitude, radius):
    return Bomb.objects.filter(longitude__gte=longitude-radius, longitude__lte=longitude+radius,
                               latitude__gte=latitude-radius, latitude__lte=latitude+radius)