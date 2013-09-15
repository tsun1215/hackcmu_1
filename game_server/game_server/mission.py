def generate_friendlys(player_list,targets):
	import random, copy
	friendlys = {}
	if len(player_list) > 4:
		for player in player_list:
			friendlys_list = []
			pos_friendly = copy.copy(player_list)
			pos_friendly.remove(player)
			pos_friendly.remove(targets[player])
			for i in xrange(3):
				temp = random.choice(pos_friendly)
				friendlys_list.append(temp)
				pos_friendly.remove(temp)
			friendlys[player] = friendlys_list
	return friendlys

def generate_targets(player_list):
	import random, copy
	targets = {}
	pos_target = copy.copy(player_list)[1:]
	random.shuffle(pos_target)
	cur = player_list[0]
	while(len(pos_target)>0):
		targets[cur] = pos_target[0]
		cur = pos_target[0]
		pos_target = pos_target[1:]
	targets[cur] = player_list[0]
	return targets

def generate_mission(player_list):
	targets = generate_targets(player_list)
	friendlys = generate_friendlys(player_list,targets)
	return [{player:{"target":targets[player],"friendlys":friendlys[player]}} for player in player_list]
