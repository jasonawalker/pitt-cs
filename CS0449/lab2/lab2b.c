#include <stdio.h>

int main(){
	printf("Please enter the weight you'd like to convert: ");
	int  w;
	scanf("%d", &w);
	printf("\nHere is the weight on other planets:\n\n");
	printf("Mercury\t\t%g lbs\n", w * 0.38f);
	printf("Venus\t\t%g lbs\n", w * 0.91f);
	printf("Mars\t\t%g lbs\n", w * 0.38f);
	printf("Jupiter\t\t%g lbs\n", w * 2.54f);
	printf("Saturn\t\t%g lbs\n", w * 1.08f);
	printf("Uranus\t\t%g lbs\n", w * 0.91f);
	printf("Neptune\t\t%g lbs\n", w * 1.19f);
	return 0;
}
