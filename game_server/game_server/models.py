from django.db import models


class Player(models.Model):
    username = models.CharField(max_length=100, unique=True)
    device_id = models.CharField(max_length=2000, unique=True)

    first_name = models.CharField(max_length=100, default="")
    last_name = models.CharField(max_length=100, default="")

    last_longitude = models.FloatField(null=True, blank=True)
    last_latitude = models.FloatField(null=True, blank=True)
    last_altitude = models.FloatField(null=True, blank=True)
    last_seen = models.DateTimeField(null=True, blank=True)

    bombs_remaining = models.IntegerField(default=1)


    class Meta:
        verbose_name = 'Player'
        verbose_name_plural = 'Players'

    def __unicode__(self):
        return self.username

    def get_last_loc(self):
        return last_longitude, last_latitude, last_altitude

    def place_bomb(self, longitude, latitude, altitude):
        if self.bombs_remaining > 0:
            new_bomb = Bomb(placed_by=self, longitude=longitude, latitude=latitude, altitude=altitude)
            new_bomb.save()
            self.bombs_remaining-=1
            return new_bomb
        else:
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
