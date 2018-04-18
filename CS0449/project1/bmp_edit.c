// Jason Walker
#include <stdio.h>
#include <string.h>
#include <math.h>
#pragma pack(1)

struct BMPheader {
	char format[2];
	int size;
	short res1;
	short res2;
	int offset;
};

struct DIBheader {
	int header_size;
	int width;
	int height;
	short color_planes;
	short bpp;
	char compression[4];
	int img_size;
	int h_res;
	int v_res;
	int num_colors;
	int imp_colors;

};

struct pixel {
	unsigned char blue;
	unsigned char green;
	unsigned char red;
};

int main(int argc, char const *argv[]) {
	//Opens file for read and write in binary mode
	FILE *img = fopen(argv[2], "rb+");
	struct BMPheader header1;
	struct DIBheader header2;
	struct pixel px;
	int i;
	int j;
	float y;

	//read both headers at once
	fread(&header1, sizeof(header1), 1, img);
	fread(&header2, sizeof(header2), 1, img);


	//check that the filetype is supported
	if(strncmp(header1.format, "BM", 2) == 0 && header2.header_size == 40 && header2.bpp == 24){
		//move pointer to where the pixels start
		fseek(img, header1.offset, SEEK_SET);

		//loop through in 2 dimensions, this has to be done because of the padding at the end
		for(i = 0; i < header2.height; i++){
			for(j = 0; j < header2.width; j++){
				//read a single pixel at the pointer
				fread(&px, sizeof(px), 1, img);

				if(strcmp(argv[1], "-invert") == 0){
					//not operator on RBG values
					px.blue = ~px.blue;
					px.green = ~px.green;
					px.red = ~px.red;
				} else if(strcmp(argv[1], "-grayscale") == 0){
					//arithmetic for determining the single rbg value
					y = 0.2126 * ((float)px.red / 255) + 0.7152 * ((float)px.green / 255) + 0.0722 * ((float)px.blue / 255);

					if(y <= 0.0031308){
						y *= 12.92;
					} else {
						y = (1.055 * pow(y, 1/2.4)) - 0.055;
					}

					px.blue = (char)(y * 255);
					px.green = (char)(y * 255);
					px.red = (char)(y * 255);
				} else { //terminate program if the command is not -invert or -grayscale
					printf("ERROR: Command not supported.");
					return 0;
				}

				//move the pointer back 3 bits
				fseek(img, -3, SEEK_CUR);
				fwrite(&px, sizeof(px), 1, img);
			}
			//arithmetic for determinig how much padding is at the end, and then moving the pointer past it before looping again
			fseek(img, (header2.width*3) % 4, SEEK_CUR);
		}


	} else {
		//terminate program if the conditions are not met
		printf("ERROR: Filetype not supported.");
	}

	return 0;
}

