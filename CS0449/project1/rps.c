//Jason Walker
#include <stdio.h>
#include <string.h>

int getComputerChoice(char* str);
int getPlayerChoice(char* str);
void getResult(int p, int c, char* str, int* ps, int* cs);
void convertToLower(char* str);


int main() {
	srand((unsigned int)time(NULL));

	int playing = 0;
	char resp[20];
	char compString[20];
	//Holds result text to be outputted to the user
	char result[50];
	//Choice variables
	int plyrChoice;
	int compChoice;
	//Score Varibales
	int plyr = 0;
	int comp = 0;

	printf("Welcome to Rock, Paper, Scissors\n\nWould you like to play? ");
	scanf("%s", resp);
	convertToLower(resp);
	//"yes" is the only acceptable answer for continuing, everything else terminates the program
	if(strcmp(resp,"yes") == 0){
		playing = 1;
	}

	//Game loop
	while(playing){
		printf("\nWhat is your choice? ");
		scanf("%s", resp);
		plyrChoice = getPlayerChoice(resp);
		compChoice = getComputerChoice(compString);
		//checks if the player response is valid, because getPlayerChoice returns 0 if it is not valid
		if(plyrChoice != 0){
			getResult(plyrChoice, compChoice, result, &plyr, &comp);
			printf("\nThe computer chooses %s. %s\n\nThe score is now you: %d computer: %d", compString, result, plyr, comp);
		} else {
			//prompt again if invalud
			printf("We did not recognize your answer. Please choose rock/paper/scissors.");
		}

		//ends the game once one person gets to 3
		if(plyr == 3 || comp == 3){
			printf("\nGame Over!");
			//updates playing var to stop the game loop
			playing = 0;
		}
	}

	printf("\nGoodbye!\n");
	return 0;
}

/*
*	1=scissors
*	2=Rock
*	3=paper
*/

/*
* getComputerChoice returns an integer value corresponding to the choice a computer makes relative to the key above
* it also stores a string version of the choice for purposes of being outputted to the user
*/
int getComputerChoice(char* str){
	int value = rand() % (3 - 1 + 1) + 1;
	switch (value) {
		case 1:
			strcpy(str, "scissors");
			break;
		case 2:
			strcpy(str, "rock");
			break;
		case 3:
			strcpy(str, "paper");
			break;
		default:
			strcpy(str, "rock");
			break;
	}
	return value;

}

//returns a number corresponding to the players choice, and 0 if it is not r/p/s
int getPlayerChoice(char* str){
	convertToLower(str);

	if(strcmp(str, "scissors") == 0){
		return 1;
	} else if (strcmp(str, "rock") == 0){
		return 2;
	} else if (strcmp(str, "paper") == 0){
		return 3;
	} else {
		return 0;
	}
	return 0;
}


//converts a string to lowercase, for the sake of ScIssOrS still being an acceptable answer, for example
void convertToLower(char *str){
	int i;
	for(i = 0; str[i]; i++){
		str[i] = tolower(str[i]);
	}
}

//compares the numbers of the two choices, updates the score, and updates the string to be outputted to the user
void getResult(int p, int c, char* str, int* ps, int* cs){
	if(p==1){
		if(c==1){
			strcpy(str, "It's a tie!");
		}
		else if(c==2){
			strcpy(str, "You lose this game!");
			(*cs)++;
		}
		else if(c==3){
			strcpy(str, "You win this game!");
			(*ps)++;
		}
	}

	else if(p==2){
		if(c==1){
			strcpy(str, "You win this game!");
			(*ps)++;
		}
		else if(c==2){
			strcpy(str, "It's a tie!");
		}
		else if(c==3){
			strcpy(str, "You lose this game!");
			(*cs)++;
		}
	}

	else if(p==3){
		if(c==1){
			strcpy(str, "You lose this game!");
			(*cs)++;
		}
		else if(c==2){
			strcpy(str, "You win this game!");
			(*ps)++;
		}
		else if(c==3){
			strcpy(str, "It's a tie!");
		}
	}
}

