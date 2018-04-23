#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "pi.h"


int main(int argc, char const *argv[]) {
	//varibles that hold the arguments and important information about the arguments
	int start;
	int end;
	int length;
	FILE *driver;

	//atoi() turns ascii into an integer, which is used on that arguments to make them ints
	start = atoi(argv[1]);
	end = atoi(argv[2]);

	//check if the user gave 2 arguments that are positive and the first one is smaller
	if (argc != 3) {
		printf("enter in the appropriate emount of args");
		return 0;
	}else if(start<0){
		printf("enter a positive number\n");
		return 0;
	} else if(end<0){
		printf("enter a positive number\n");
		return 0;
	} else if(start>end){
		printf("make sure end is bigger than start\n");
		return 0;
	}

	//set the appropriate length
	length = end-start+1;

	//digits will recieve the output of the driver
	char digits[end+1];
	// pi(digits, end+4);
	driver = fopen("/dev/pi", "r");

	//fseek moves the ppos to where it should be
	fseek(driver, start, SEEK_SET);
	fread(digits, end+1, 1, driver);
	fclose(driver);

	//nul terminate the string
	digits[end+1] = '\0';
	//prints digits, with formatting assuming start is not 0
	printf("%.*s\n", length, digits + start);

	return 0;
}
