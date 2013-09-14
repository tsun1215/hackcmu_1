from django.http import HttpResponse, HttpResponseBadRequest
import simplejson
import datetime
from django.utils.timezone import utc


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

# Returns the bombs within a specified radius of a location
def get_bombs(request):
	if request.method == "POST":
		return HttpResponse(simplejson.dumps({"test": [1,2,3]}), content_type="application/json")
	else:
		return HttpResponseBadRequest()


# Handles when a player places a bomb
def place_bomb(request):
	if request.method == "POST":
		return HttpResponse(simplejson.dumps({"test": [1,2,3]}), content_type="application/json")
	else:
		return HttpResponseBadRequest()


# Handles when a player is hit by a bomb
def bomb_player(request):
	if request.method == "POST":
		return HttpResponse(simplejson.dumps({"test": [1,2,3]}), content_type="application/json")
	else:
		return HttpResponseBadRequest()