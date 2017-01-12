/*
In this assignment from my data structures and algorithms class, I reconstruct a tree using its postorder
and inorder traversals, and print its levelorder for the output.
*/


#include <string>
#include <iostream>
#include <queue>
#include <stdlib.h>

using namespace std;

struct Node
{
    int data;
    Node* left;
	Node* right;
};

Node* createN(int data);

int findInd(int nums[], int start, int end, int value)
{
    int ans = 0;
    for (int i = start; i <= end; i++)
    {
    	//
        if (nums[i] == value)
        {
        	ans = i;
            break;
        }
    }
    return ans;
}

Node* createT(int in[], int post[], int inStart, int inEnd, int *postIndex)
{
    if (inStart > inEnd)
        return nullptr;
    Node *node = createN(post[*postIndex]);
    (*postIndex)--;
    if (inStart == inEnd)
        return node;
    int iIndex = findInd(in,inStart,inEnd,node->data);
    node->right= createT(in, post, iIndex+1, inEnd,postIndex);
//node->left = createT(in, post,???);
    node->left = createT(in, post, inStart, iIndex-1,postIndex);
    return node;
}

Node *buildTree(int in[], int post[], int n)
{
	int indx = n-1;
	//int jn
    return createT(in, post, 0, n - 1, &(indx));
}

Node* createN(int data)
{
    Node* node = (Node*)malloc(sizeof(Node));
    //node->data = no->
    node->data=data;
    node->left=node->right=nullptr;
    return node;
}

void printLevelOrder(Node *root)
{
    if (root == nullptr)
    	return;
    queue<Node *> q;
    q.push(root);
    while (!q.empty())
    {
        Node *node = q.front();
        cout << node->data << " ";
        q.pop();
        if (node->left != nullptr)
            q.push(node->left);
        if (node->right != nullptr)
            q.push(node->right);
    }
}

int main()
{
    int N;
    	cin >> N;
    	int post[N];
    	int in[N];
    	//vector<int> v;

    	int n = sizeof(in)/sizeof(in[0]);
    	for(int x = 0; x < 2; x++)
    	{
    		for(int j = 0; j < N;j ++)
    		{
    		if(x == 0)
    			cin >> post[j];
    		else
    			cin >> in[j];
    		}
    	}
    Node * finalgRoot = buildTree(in, post, n);
    printLevelOrder(finalgRoot);

    return 0;
}