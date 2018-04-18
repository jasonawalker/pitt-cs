#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include "mymalloc.h"

/*
 * Node structure that holds the bookeeping information
 * size: size in bytes
 * empty: 0 = full; 1 = empty
 * next: next Node in list
 * prev: previous Node in list (doubly linked)
 */

typedef struct Node *node;

struct Node //size 4+1+4+4 = 13 bytes
{
  int size;
  char empty;
  node next;
  node prev;
};

// Varibles that hold the beginning and end node of the linked list
static node head = NULL;
static node tail = NULL;

const NODE_SIZE = sizeof(struct Node);

//Fucntion that uses the worst fit agorithm and returns a node pointer to the space, null if no space exists
node find_worst_fit(int size){

	node current = head;
	node worst = NULL;

	while(current != NULL){
		if(current->empty == 1 && current->size >= size && (worst == NULL || worst->size < current->size)){
			worst = current;
		}
		current = current->next;
	}


	return worst;
}

//increases the brk variable when find_worst_fit returns NULL
node increase_heap(int size){
	node t;
	t = sbrk(0);

	if (sbrk(NODE_SIZE + size) == (void*)-1)
    	return NULL;

	t->size = size;
	t->empty = 0;
	t->next = NULL;
	t->prev = tail;

	if(head == NULL){
		head = t;
		tail = t;
	} else {
		tail->next = t;
		tail = t;
	}

	return t;
}

//coalesces multiple empty nodes into one node
void combine(node curr){
	if(curr != NULL && curr->empty == 1 && curr->next->empty == 1){
		curr->size = curr->next->size + curr->size + NODE_SIZE;
		curr->next = curr->next->next;
		curr->next->prev = curr;
	}
}

//called when a worst fitting node is found
node allocate(int size, node n){
	if((n->size == size) || ((n->size - size) <= NODE_SIZE)){
		n->empty = 0;
		return n;
	} else {

		int temp_size = n->size;
		node temp = (void *)n + NODE_SIZE + size;

		temp->next = n->next;
		n->next = temp;

		temp->prev = n;

		n->size = size;
		temp->size = temp_size - NODE_SIZE- size;

		temp->empty = 1;
		n->empty = 0;

		return n;
	}
}

//myMalloc
void *my_worstfit_malloc(int size) {

	node temp;

	if(head == NULL){
		temp = increase_heap(size);
	} else {
		temp = find_worst_fit(size);

		if(temp == NULL){
			temp = increase_heap(size);
		} else {
			temp = allocate(size, temp);
		}

	}

	return (void *)temp + NODE_SIZE;
}

//myfree
void my_free(void *ptr) {
	node temp = (void *)ptr - NODE_SIZE;

	temp->empty = 1;
	if(temp->next == NULL){
		if(temp->prev == NULL){
			head = NULL;
			tail = NULL;
		} else {
			tail = temp->prev;
			if(tail->empty == 1){
				tail = tail->prev;
				sbrk(-(temp->prev->size + NODE_SIZE));
			}
			tail->next = NULL;
		}
		sbrk(-(temp->size + NODE_SIZE));
	} else {
		combine(temp);
		combine(temp->prev);
	}


}
