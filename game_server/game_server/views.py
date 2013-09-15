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
# - Handle deletion of a game room (if players no longer exist in it)
# - Deal with scoring

import logging
logger = logging.getLogger(__name__)
def register_device(request):
    if request.method == "POST":
        try:
            new_player = Player.objects.get(device_id=request.POST.get('device_id'))
        except ObjectDoesNotExist:
            new_player = Player(username=request.POST.get("username"), 
                                first_name=request.POST.get("f_name"), 
                                last_name=request.POST.get("l_name"), 
                                device_id=request.POST.get("device_id"))
            new_player.save()
        return HttpResponse(simplejson.dumps({"success": True, 
                                              "device_id": new_player.device_id, 
                                              "username": new_player.username, 
                                              "f_name":new_player.first_name, 
                                              "l_name":new_player.last_name}), 
                                            content_type="application/json")
    else:
        return HttpResponseBadRequest("Not a post request")


def create_game(request):
    new_game = Game(name=request.POST.get("game"),
                    longitude=request.POST.get("long"),
                    latitude=request.POST.get("lat"),
                    is_public=request.POST.get("public",False))
    new_game.add_player(Player.objects.get(device_id=request.POST.get("device_id")))
    return HttpResponse(simplejson.dumps({"success": True, "game_id": new_game.pk}), content_type="application/json")


def list_games(request):
    # Lists the games within a certain radius of a given longitude and latitude
    matched_games = []
    for g in Game.objects.all():
        matched_games.append({"id": g.pk,
                              "name": g.name,
                              "players": g.player_set.all().count(),
                              })
    return HttpResponse(simplejson.dumps({"games":matched_games}), content_type="application/json")


def join_game(request):
    if request.method == "POST":
        try:
            player = Player.objects.get(device_id=request.POST.get('device_id'))
            player.clear_game_info()
        except ObjectDoesNotExist:
            # Return error telling user to register
            return
        try:
            game = Game.objects.get(pk=request.POST.get('game_id'))
        except ObjectDoesNotExist:
            # Return error telling that game doesn't exist
            return
        game.add_player(player)
        return HttpResponse(simplejson.dumps({"success": True, "game": game.pk}), content_type="application/json")
        

# Returns the bombs within a specified radius of a location
def get_bombs(request):
    if request.method == "POST":
        bombs = [b.get_info() for b in bombs_in_radius(float(request.POST.get("long")), float(request.POST.get("lat")), float(request.POST.get("rad")))]
        return HttpResponse(simplejson.dumps({"bombs": bombs}), content_type="application/json")
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