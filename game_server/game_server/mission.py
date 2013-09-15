def GenerateFriendlies(PlayerList,Targets):
	import random, copy
	Friendlies = {}
	if len(PlayerList) > 4:
		for player in PlayerList:
			FriendliesList = []
			posFriendly = copy.copy(PlayerList)
			posFriendly.remove(player)
			posFriendly.remove(Targets[player])
			for i in xrange(3):
				temp = random.choice(posFriendly)
				FriendliesList.append(temp)
				posFriendly.remove(temp)
			Friendlies[player] = FriendliesList
	return Friendlies
def GenerateTargets(PlayerList):
	import random, copy
	posTarget = copy.copy(PlayerList)
	Targets = {}
	for player in PlayerList:
		temp = random.choice(posTarget)
		while(temp == player):
			temp = random.choice(posTarget)
		posTarget.remove(temp)
		Targets[player] = temp
	return Targets
def GenerateMission(PlayerList):
	Targets = GenerateTargets(PlayerList)
	Friendlies = GenerateFriendlies(PlayerList,Targets)
	return(Targets,Friendlies)
	
def main():
	N=5
	a = "{Num} Players".format(Num=N)
	PlayerList = ['0','1','2','3','4']
	for Dict in GenerateMission(PlayerList):
		print(Dict)
