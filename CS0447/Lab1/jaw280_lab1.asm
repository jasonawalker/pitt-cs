# Jason Walker (jaw280)
print_int:
	li	v0, 1
	syscall
	jr	ra
	
newline:
	li	a0, 10
	li	v0, 11
	syscall
	jr	ra

.globl main
main:
	li 	a0, 1234
	li	v0, 1
	syscall
	jal	newline
	li	a0, 5678
	jal	print_int
	
# public static voud main(String[] args){
# 	int a0 = 1234;
#	System.out.println(a0);
# }

