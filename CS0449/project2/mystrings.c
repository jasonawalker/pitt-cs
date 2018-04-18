// Jason Walker
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <ctype.h>

int main(int argc, char const *argv[]) {
	//open the file and save it under var txt (for read in binary format)
	FILE *txt = fopen(argv[1], "rb");

	//with 1000 characters, the string will most likely never run out of space
	char buffer[1000];
	//variable holds current character
	char current;

	//loop while we are not at the end of the file
	while(!feof(txt)){
		//set current
		current = fgetc(txt);
		//<ctype.h> function checks if it is printable
		if(isprint(current)){
			//add to buffer
			strncat(buffer, &current, 1);
		} else {
			if(strlen(buffer) > 3){
				//if the length is 4 or more, print it
				printf("%s\n", buffer);
			}
			//clear the buffer by setting the first char to the nul-terminator
			buffer[0] = '\0';
		}
	}

	return 0;
}
