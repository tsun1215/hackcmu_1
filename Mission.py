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
	Targets = {}
	posTarget = copy.copy(PlayerList)[1:]
	random.shuffle(posTarget)
	cur = PlayerList[0]
	while(len(posTarget)>0):
		print(cur)
		print(posTarget[0])
		Targets[cur] = posTarget[0]
		cur = posTarget[0]
		posTarget = posTarget[1:]
	Targets[cur] = PlayerList[0]
	print(Targets)
	return Targets
def GenerateMission(PlayerList):
	Targets = GenerateTargets(PlayerList)
	Friendlies = GenerateFriendlies(PlayerList,Targets)
	return {player:[Targets[player],Friendlies[player]] for player in PlayerList}
def main():
	N=5
	a = "{Num} Players".format(Num=N)
	PlayerList = ['123a','123b','123c','123d','123e']
	print(GenerateMission(PlayerList))
main()
