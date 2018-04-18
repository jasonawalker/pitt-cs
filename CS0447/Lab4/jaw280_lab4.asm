.data
	opcode: .asciiz "\nopcode = "
	rs: .asciiz "\nrs = "
	rt: .asciiz "\nrt = "
	immediate: .asciiz "\nimmediate = "

.text

encode_instruction:
	push	ra

	sll t0, a0, 26
	sll t1, a1, 21
	sll t2, a2, 16

	andi a3, a3, 0x0000FFFF
	
	or t0, t0, t1
	or t0, t0, t2
	or t0, t0, a3
	
	li v0, 34
	move a0, t0
	syscall
	li v0, 11
	li a0, 10
	syscall

	pop	ra
	jr	ra

decode_instruction:
	push	ra
	
	#opcoode
	push a0
	la a0, opcode
	li v0, 4
	syscall
	pop a0
	
	srl t0, a0, 26
	push a0
	move a0, t0
	li v0, 1
	syscall
	pop a0
	
	#rs
	push a0
	la a0, rs
	li v0, 4
	syscall
	pop a0
	
	srl t0, a0, 21
	push a0
	and a0, t0, 0x1f
	li v0, 1
	syscall
	pop a0
	
	#rt
	push a0
	la a0, rt
	li v0, 4
	syscall
	pop a0
	
	srl t0, a0, 16
	push a0
	and a0, t0, 0x1f
	li v0, 1
	syscall
	pop a0

	#immediste
	push a0
	la a0, immediate
	li v0, 4
	syscall
	pop a0
	
	move t0, a0
	push a0
	sll t0, t0, 16
	sra t0, t0, 16 
	move a0, t0
	li v0, 1
	syscall
	pop a0


	pop	ra
	jr	ra

.globl main
main:
	# addi t0, s1, 123
	li	a0, 8
	li	a1, 17
	li	a2, 8
	li	a3, 123
	jal	encode_instruction

	# beq t0, zero, -8
	li	a0, 4
	li	a1, 8
	li	a2, 0
	li	a3, -8
	jal	encode_instruction

	li	a0, 0x2228007B
	jal	decode_instruction

	li	a0, '\n'
	li	v0, 11
	syscall

	li	a0, 0x1100fff8
	jal	decode_instruction

	# exit the program cleanly
	li	v0, 10
	syscall
