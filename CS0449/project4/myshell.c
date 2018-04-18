#include <sys/times.h>
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <inttypes.h>

//Varibales cooresponding to the maximum length of an input to the shell, and the delimiter characters
static const int MAX_LENGTH = 150;
static const char delim[13] = " \t\r\n\v\f()<|&;";

//Boolean type variables that correspond to whether the user wants to track the time or output to a file
static char track_time = 0;
static char file_out = 0;
static char file_append = 0;
static int counter = 1;

//Struct varibales that hold the times when clocked
static struct tms start_times;
static struct tms end_times;

//Function declarations to satisfy the compiler
void begin_shell();
void execute_commands(char *cmd[], int length, char *file_name);
void change_directory(char* path);
void start_clock();
void end_clock();

//Main function calls the shell to start
int main(int argc, char const *argv[]) {

	begin_shell();

	return 0;
}

//Shell function loops through the code until forced exit by command 'exit'
void begin_shell(){
	while(1) {
		char input[MAX_LENGTH]; //String holds the command enteref by the user
		char *token; //Current token returned by strtok
		int i = 0; //Length of the cmd array
		char *file_name; //Holds the name of the file if applicable, NULL otherwise

		printf("(%x) > ", counter);
		fgets(input, sizeof(input), stdin);

		if(strcmp(input, "\n") != 0){ // Checks if the user entered any words at all
			char *cmd[sizeof(input)];
			token = strtok(input, delim);

			while(token != NULL){
				if(strcmp(token, "exit") == 0){
					exit(0);
				} else if(strcmp(token, "time") == 0){ //Checks if time is called
					track_time = 1;
				} else if (strcmp(token, ">") == 0){ //Checks if output to file
					file_out = 1;
					token = strtok(NULL, delim);
					file_name = token;
					break;
				} else if (strcmp(token, ">>") == 0){ //Checks if append to file
					file_append = 1;
					token = strtok(NULL, delim);
					file_name = token;
					break;
				} else {
					cmd[i] = token;
					i++;
				}
				token = strtok(NULL, delim);
			}
			cmd[i] = NULL; //Set the last index of the cmd array to null to satisfy execvp()
			execute_commands(cmd, i, file_name);
		}
	}

}

//Forks and calls all necesary functions and implements time, >, and >>
void execute_commands(char *cmd[], int length, char *file_name){
		char *command = cmd[0];
		FILE *file;
		pid_t cpid;

		if(track_time == 1){
			start_clock();
		}

		if(strcmp(command, "cd") == 0){
			change_directory(cmd[1]);
		} else {
			cpid = fork();
			if(cpid == 0){
				if(file_out){
					file = freopen(file_name, "w", stdout);
				} else if(file_append){
					file = freopen(file_name, "a", stdout);
				}

				if(!file && (file_out || file_append)){
					perror("Error");
					exit(0);
				}

				execvp(command, cmd);
				perror("Error");
				// printf("shell: command not found: %s\n", command);
				exit(0);
			} else {
				int status;
				if(cpid == -1 || wait(&status) == -1){
					perror("Error");
				}
			}
		}
		end_clock();
		counter++;
}

//Seperate function to implement 'cd' by using the syscall chdir()
void change_directory(char* path){
	int success = chdir(path);
	if(success == -1){
		// printf("cd: no such file or directory: %s\n", path);
		perror("Error");
	}
}

//Starts the timer and checks for error
void start_clock(){
	clock_t stime = times(&start_times);
	if(stime == -1){
		perror("Error");
	}
}

//Ends the timer, prints the numbers out, and resets all file variables
void end_clock(){
	if(track_time == 1){
		clock_t etime = times(&end_times);
		if(etime == -1){
			perror("Error");
		}

		float user_time = (float)(end_times.tms_cutime - start_times.tms_cutime)/sysconf(_SC_CLK_TCK);
		float sys_time = (float)(end_times.tms_cstime - start_times.tms_cstime)/sysconf(_SC_CLK_TCK);

		printf("User Time: %.5lf, System Time: %.5lf\n", user_time, sys_time);
		track_time = 0;
	}
	file_out = 0;
	file_append = 0;
}
