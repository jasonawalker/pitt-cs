#Jason Walker

.data
#variables go here
	small: .byte 200
	medium: .half 400
	large: .word 0
	
	.eqv NUM_ITEMS 5
	values: .word 0:NUM_ITEMS
.text
.globl main
main:
	lbu a0, small
	lhu a1, medium
	
	mul a2, a0, a1
	sw a2, large
	
	li v0, 1
	lw a0, large
	syscall 
	
	li s0, 0
	ask_loop_top:
		bge s0, NUM_ITEMS, ask_loop_exit
	ask_loop_body:
		add s0, s0, 1
		
		li v0, 5
		syscall
		
		#find address
		la t0, values
		mul t1, s0, 4
		add t1, t1, t0
		sw v0, (t1)
		
		b ask_loop_top
	ask_loop_exit:

