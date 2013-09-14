from django.http import HttpResponse, HttpResponseBadRequest
from django.core.exceptions import ObjectDoesNotExist
import simplejson
import datetime
from django.utils.timezone import utc
from game_server.models import *


def test(request):
    import random
    locs = []
    for x in range(5):
        locs.append({
            "long": random.uniform(-180,180),
            "lat": random.uniform(-90, 90),
            "altitude": random.uniform(0, 100),
            "time_placed": (datetime.datetime.utcnow().replace(tzinfo=utc) - datetime.timedelta(hours=random.randint(0, 120))).isoformat(),
            "radius": 10,
            "placed_by": "239r2e90efj%d" % random.randint(5,8),
        })
    return HttpResponse(simplejson.dumps(locs), content_type="application/json")


# TODO:
# - Handle creation of a game room
# - Handle joining a game room
# - Handle deletion of a game room (if players no longer exist in it)
# - Handle the first device/user registration

def join_game(request):
    if request.method == "POST":
        try:
            player = Player.objects.get(device_id=request.POST.get('device_id'))
        except ObjectDoesNotExist:
            # Return error telling user to register
            return
        try:
            game = Game.objects.get(pk=request.POST.get('game_id'))
        except ObjectDoesNotExist:
            # Return error telling that game doesn't exist
            return
        player.game = game
        player.save()
        return HttpResponse(simplejson.dumps({"success": True, "game": game.pk}), content_type="application/json")
        

# Returns the bombs within a specified radius of a location
def get_bombs(request):
    if request.method == "POST":
        bombs = [b.get_info() for b in bombs_in_radius(float(request.POST.get("long")), float(request.POST.get("lat")), float(request.POST.get("rad")))]
        return HttpResponse(simplejson.dumps(bombs), content_type="application/json")
    else:
        return HttpResponseBadRequest()


# Handles when a player places a bomb
def place_bomb(request):
    if request.method == "POST":
        try:
            player = Player.objects.get(device_id=request.POST.get('device_id'))
        except ObjectDoesNotExist:
            # Return error telling user to register
            return
        bomb = player.place_bomb(request.POST.get("long"), request.POST.get("lat"), request.POST.get("alt"))
        if bomb == None:
            # Error Code 1: Not enough bombs
            return HttpResponse(simplejson.dumps({"success": False, "error_code": 1}))
        return HttpResponse(simplejson.dumps({"success": True, "bomb": bomb.get_info()}), content_type="application/json")
    else:
        return HttpResponseBadRequest()


# Handles when a player is hit by a bomb
def bomb_player(request):
    if request.method == "POST":
        return HttpResponse(simplejson.dumps({"test": [1,2,3]}), content_type="application/json")
    else:
        return HttpResponseBadRequest()