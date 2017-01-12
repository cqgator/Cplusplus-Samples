/*
This was an assignment for my Programming Fundamentals 2 class. The assignment was to write a 
C++ program that creates three (3) different N*N magic squares. A square
matrix is the arrangement of the numbers 1, 2, , , in which the sum of rows, columns, and
diagonals are the same. The users will specify the size of the square matrix:
N. The value N must be an odd number between 3 and 15.
*/


//This is necessary for the input and output.
#include <iostream>
#include <cstdlib>
//These are necessary for the setw(int) commands for spacing
#include <fstream>
#include <iomanip>

using namespace std;
int main() {
	cout << "Enter the size of a magic square: ";
	int N;
	std::cin >> N;

	//This while loop ensures that the input is a valid number between 3 and 15
	//before declaring an array.
	while (cin.fail() || N > 15 || N < 3 || N%2==0) {
		std::cin.clear();
		std::cin.ignore(256,'\n');
		cout << "Enter the size of a magic square: ";
		std::cin >> N;
	}
	int nums[N][N];
	int n, m;
	//This nested for loop fills the 2d square matrix with zeros
	for (n = 0; n < N; n++)
		for (m = 0; m < N; m++)
			nums[n][m] = 0;
	//sC and sR are "starting column" and "starting row", respectively.
	//These are the starting indexes at which the array is filled, and the
	//indexes that are used to fill the array at the start of the following while loop.
	int sC = (N / 2);
	int sR = 0;
	int count = 1;
	int max = N * N;
	//This while loop fills the first magic square correctly.
	//"count" is the integer between 1 and N, inclusively, that is being placed in the square matrix.
	//This while loop fills the magic square with "count" going from 1 to N.
	//nR and nC are "new row" and "new column". In the while loop, these are computed as
	//the next index to which the following "count" number should be placed.
	//nR and nC are necessary for the cases in which a number is already in the index that is
	//meant for the next "count" number.
	//
	//The construction of this first magic square is as follows: The numbers are filled in the matrix in
	//increasing order, starting in the central column of the first row having the number 1, with the next number
	//filling in the spot diagonally up and to the right, wrapping around the the last row or first column if the
	//next move is in a spot outside of the matrix.
	while (count <= max) {
		nums[sR][sC] = count;
		int nR = 0;
		int nC = 0;
		if (sR == 0)
			nR = N - 1;
		else
			nR = (sR - 1) % N;
		nC = (sC + 1) % N;
		//This checks if there is a value in the index that the next "count" number should be placed in.
		//nums[nR][nC] will equal 0 if there have been no number placed there. If a number is there, instead,
		//the next number to be filled in will be placed in the spot one unit vertically down, instead of going
		//up and to the right.
		while (nums[nR][nC] != 0) {
			nR = sR + 1;
			nC = sC;
			sR = nR;
			sC = nC;
		}
		sR = nR;
		sC = nC;
		count++;
	}

	//At this point, the first magic square has already been completed.
	//This for loop iterates three times to print out the first magic square and its sums,
	//Then compute and print out the other two magic squares and their sums
	for (int we = 0; we < 3; we++) {
		//These are the two arrays with the sums for each row and column, respectively.
		int rSums[N];
		int cSums[N];
		cout << "\n" << "Magic Square #" << (we + 1) << " is:";
		cout << "\n" << "\n";
		//This for loop both computes the row and column sums while printing out the magic square.
		//This same for loop prints out each of the three magic squares.
		for (int i = 0; i < N; ++i) {
			int rSum = 0;
			int cSum = 0;
			for (int j = 0; j < N; ++j) {
				std::cout << setw(4) << nums[i][j] << ' ';
				rSum += nums[i][j];
				cSum += nums[j][i];
			}
			std::cout << std::endl;
			rSums[i] = rSum;
			cSums[i] = cSum;
		}
		std::cout << "\n";
		cout << "Checking the sums of every row:" << setw(6) << right << " ";
		//This prints out each value of the array containing the collection of sums of every row.
		for (int x = 0; x < N; x++) {
			std::cout << rSums[x] << " ";
		}
		std::cout << "\n";
		cout << "Checking the sums of every column:" << setw(3) << right << " ";
		//This prints out each value of the array containing the collection of sums of every column.
		for (int x = 0; x < N; x++) {
			std::cout << cSums[x] << " ";
		}
		std::cout << "\n";

		int dOne = 0;
		int dTwo = 0;
		//These for loops compute the diagonal sums.
		//dOne is the diagonal sum computed from the top left corner of the square to the bottom right.
		//dTwo is the diagonal sum computed from the bottom left corner of the square to the top right.
		for (int i = 0; i < N; ++i) {
			for (int j = 0; j < N; ++j) {
				if (i == j)
					dOne += nums[i][j];
				if (j == N - 1 - i)
					dTwo += nums[i][j];
			}
		}
		cout << setw(3) << right << "Checking the sums of every diagonal: ";
		//This outputs the diagonal values.
		std::cout << dOne << " " << dTwo << "\n";
		//If "we" is 0, this is directly after the first magic square has been printed out
		//along with its sums. The nested for loop following the if statement switches the
		//magic square around to create the second magic square.
		if (we == 0)
			for (int i = 0; i < N; ++i) {
				for (int j = 0; j < N - i; ++j) {
					int temp = nums[i][j];
					nums[i][j] = nums[N - 1 - j][N - 1 - i];
					nums[N - 1 - j][N - 1 - i] = temp;
				}
			}
		//If "we" is 0, this is directly after the second magic square has been printed out
		//along with its sums. The nested for loop following the if statement switches the
		//magic square around to create the third and final magic square.
		if (we == 1)
			for (int i = 0; i < N; ++i) {
				for (int j = 0 + i; j < N; ++j) {
					if (i != j) {
						int temp = nums[i][j];
						nums[i][j] = nums[j][i];
						nums[j][i] = temp;
					}
				}
			}

	}
	return 0;
}
