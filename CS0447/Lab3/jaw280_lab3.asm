#Jason Walker
.include	"led_keypad.asm"

.data
	x_coord: .word 32
	y_coord: .word 32
	
.text
.globl main
main:
	main_loop:
		li a0, 16
		li v0, 32
		syscall
		jal check_input
		jal draw_dot
		jal display_update_and_clear
		b main_loop
		
		
draw_dot:
	push ra
	
	lw a0, x_coord
	lw a1, y_coord
	li a2, 14
	jal display_set_pixel
	
	pop ra
	jr ra
	
check_input:
	push ra
	
	jal input_get_keys
	
	beq v0, 4, move_left
	beq v0, 8, move_right
	beq v0, 1, move_up
	beq v0, 2, move_down
	b dont_move
	
	move_left:
		lw t1, x_coord
		sub t1, t1, 1
		sw t1, x_coord
		b dont_move
	move_right:
		lw t1, x_coord
		add t1, t1, 1
		sw t1, x_coord
		b dont_move
	move_up:
		lw t1, y_coord
		sub t1, t1, 1
		sw t1, y_coord
		b dont_move
	move_down:
		lw t1, y_coord
		add t1, t1, 1
		sw t1, y_coord
		b dont_move
	dont_move:
		lw t1, x_coord
		lw t2, y_coord
		andi t1, t1, 63
		andi t2, t2, 63
		sw t1, x_coord
		sw t2, y_coord
	pop ra
	jr ra