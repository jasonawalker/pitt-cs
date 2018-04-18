//Jason Walker
#include <stdio.h>
#include <string.h>
#include <stdlib.h>

struct Node {
	int grade;
	struct Node *next;
};

double findAvg(struct Node* head){
	struct Node *curr = head->next;
	double avg = 0;
	int avgCount = 0;

	while(curr->next != NULL){
		avg += (curr->grade);
		avgCount++;
		curr = curr->next;
	}

	avg /= avgCount;
	return avg;
}

void dump(struct Node* curr){
	if(curr == NULL){
		return ;
	}
	dump(curr->next);
	free(curr);
}

int main(int argc, const *argv[]) {
	int nextGrade = 0;
	double avg;
	struct Node *head = malloc(sizeof(struct Node));
	head->grade = 0;
	head->next = NULL;
	struct Node *next = head;

	printf("Enter grades, then enter -1 to stop\n");
	while(nextGrade != -1){
		struct Node *temp = malloc(sizeof(struct Node));
		scanf("%d", &nextGrade);
		temp->grade = nextGrade;
		temp->next = NULL;
		next->next = temp;
		next = temp;
	}

	avg = findAvg(head);
	printf("%f\n", avg);
	dump(head);
	return 0;
}

