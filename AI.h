#include <iostream>
#include <stack>
#include <vector>
#include <stdlib.h>
#include <utility>
std::pair <int, int> getRandomCoordinate(int boardSize);
std::pair<int, int> getRandomParityCoordinate(int boardSize);
//void levelOne();
std::pair<int, int> aiTurn();
std::vector<Ship*> aiPlaceShips();
#define r 10
#define c 10
int board[r][c] = {0};
bool lastHit = false;
void hitAiShip(bool hit) { lastHit = hit; }
bool hasVisited[r][c];
std::vector<Ship*> aiPlaceShips(){
	//chooses three random pre-set AI boards
  //samples here in case AI board is not set manually
	int Ai = rand() % 4;
	std::vector<Ship*> shipList;
	if (Ai == 0){
		shipList.push_back(new Ship(5,8,4,true));
		shipList.push_back(new Ship(4,0,0,true));
		shipList.push_back(new Ship(3,3,2,false));
		shipList.push_back(new Ship(3,5,1,false));
		shipList.push_back(new Ship(2,8,3, false));
	}
	if (Ai == 1){
		shipList.push_back(new Ship(5,1,2,true));
		shipList.push_back(new Ship(4,4,5,false));
		shipList.push_back(new Ship(3,1,5,false));
		shipList.push_back(new Ship(3,9,5,true));
		shipList.push_back(new Ship(2,3,7, false));
	} else {
		shipList.push_back(new Ship(5,0,0,true));
		shipList.push_back(new Ship(4,5,9,false));
		shipList.push_back(new Ship(3,3,3,true));
		shipList.push_back(new Ship(3,6,3,true));
		shipList.push_back(new Ship(2,4,2, true));
	}
	return shipList;
}

////////////
// main logic
// -after random coordinate hit a ship, it creates stack of potential locations the enemy ship could be
// -potential locations include (North, East, South, West) coordinates from hit location
////////////
std::pair<int, int> aiTurn() {
    // End game check
		if ( checkIfAILost() ) {
			ui->showMessage(">> AI Lost! You win!");
			break;
		}
	std::stack <std::pair<int,int > > locs;
// creating a stack of potential locations for enemy ship to be
	bool hasMadeMove = false;
	int count = 0;
//creating 2d array to keep track of already visited
	std::pair<int, int> outputShot;
	while (hasMadeMove == false) {
		// if there is nothing in potential stack...
		if (locs.empty()) {
			std::pair<int, int> check = getRandomParityCoordinate(r);
			//adding N, E, S, W to potential stack
			if (check.first - 1 >= 0)
				locs.push(std::make_pair(check.first - 1, check.second));
			if (check.second - 1 >= 0)
				locs.push(std::make_pair(check.first, check.second - 1));
			if (check.second + 1 < r)
				locs.push(std::make_pair(check.first, check.second + 1));
			if (check.first + 1 < r)
				locs.push(std::make_pair(check.first + 1, check.second));
			outputShot = check;
			hasMadeMove = true;
		}
		
		else {
			// if we've already hit that coordinate
			if (board[locs.top().first][locs.top().second] == 3
					|| board[locs.top().first][locs.top().second] == 2) {
				//computer did not actually take a turn
				hasMadeMove = false;
				locs.pop();
	//			// generate another random coordinate
	//			getRandomParityCoordinate(r);
				// if computer hits a ship coordinate that has not already been hit before
			} else if (board[locs.top().first][locs.top().second] == 0) {
				//pops that coordinate off of potential location stack
				std::pair<int, int> temp = locs.top();
				locs.pop();
				board[temp.first][temp.second] = 2;
				outputShot = temp;
				hasMadeMove = true;
			}
		}
		hasVisited[locs.top().first][locs.top().second] = true;
		count++;
		// no more potential locations to add to stack
		if (count >= 50)
			hasMadeMove = true;
	}
	return outputShot;
}
/////////////
//finds random coordinates of even parity for computer to fire
//(implementing this because smallest ship is of size 2)
/////////////
std::pair<int, int> getRandomParityCoordinate(int boardSize) {
	std::pair<int, int> coord;
	while (true) {
		coord = getRandomCoordinate(boardSize);
		if ((coord.first + coord.second) % 2 == 0) {
			return coord;
		}
	}
	return coord;
}
bool checkIfAILost(){
		bool check = true;
		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 10; y++) {
				if (board[x][y] == 1) {
					check = false;
					break;
				}
			}
			// break out of both loops if necessary
			if (!check) break;
		}
	 	return check;
}
////////////
//finds random coordinates
////////////
std::pair<int, int> getRandomCoordinate(int boardSize) {
	std::pair<int, int> coord;
	int row = rand() % r;
	int col = rand() % c;
	return std::make_pair(row, col);
}
