#include <iostream>
#include <string>
#include <math.h>
#include <vector>
#include <algorithm>
using namespace std;

int main() {

	string hint = "";
	cin >> hint;
	//cout << hint;
	int N = -1;
	cin >> N;
	char mychar;
	vector<string> mat;
	string mat2[N];
	for (int x = 0; x < N; x++) {
		string temp = "";
		for (int y = 0; y < N; y++) {
			std::cin >> mychar;
			temp += mychar;
			mat2[y] += mychar;
		}
		mat.push_back(temp);
	}

	string test1 = "";
	string test2 = "";

	for (int x = 0; x < N; x++) {
		string search = mat[x];
		int count = 0;
		for (int y = 0; y < N; y++) {
			if (search.find(hint) == -1)
				break;
			else {
				count++;
				if (search.find(hint) + 1 < N)
					search = search.substr(search.find(hint) + 1);
			}
		}
		test1 += to_string(count);
	}

	for (int x = 0; x < N; x++) {
		string search2 = mat2[x];
		int count2 = 0;
		for (int y = 0; y < N; y++) {
			if (search2.find(hint) == -1)
				break;
			else {
				count2++;
				if (search2.find(hint) + 1 < N)
					search2 = search2.substr(search2.find(hint) + 1);
			}
		}
		test2 += to_string(count2);
	}

	int track[N + 1][N + 1];
	for (int x = 0; x <= N; x++) {
		for (int j = 0; j <= N; j++) {
			if (x == 0 || j == 0)
				track[x][j] = 0;
			else if (test1[x - 1] == test2[j - 1])
				track[x][j] = track[x - 1][j - 1] + 1;
			else
				track[x][j] = max(track[x - 1][j], track[x][j - 1]);
		}
	}
	int index = track[N][N];
	string lcs = "";
	int x = N;
	int y = N;
	while (x > 0 && y > 0) {
		if (test1[x - 1] == test2[y - 1]) {
			string temp = lcs;
			lcs = test1[x - 1] + temp;
			x--;
			y--;
			index--;
		} else if (track[x - 1][y] > track[x][y - 1])
			x--;
		else
			y--;
	}

	//string newLCS = "";
	for(int x = 0; x < lcs.length();x++)
	{
		if(x == lcs.length()-1)
			cout << lcs.substr(x,1);
		else
			cout << lcs.substr(x,1) << " ";
	}

	return 0;
}
