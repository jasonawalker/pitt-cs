#include <stdio.h>
#include <unistd.h>
#include "mymalloc.h"

void test();

int main(int argc, char *argv[]){
  int *brk;
  brk = sbrk(0);
  printf("Location 1: %d\n", brk);
  test();
  brk = sbrk(0);
  printf("Location 2: %d\n", brk);
  return 0;
}


void test(){
  int *mall1 = my_worstfit_malloc(50);
  int *mall2 = my_worstfit_malloc(50);

  my_free(mall2);

  my_free(mall1);
}
