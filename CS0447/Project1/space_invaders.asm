# jaw280
# Jason Walker

.include "convenience.asm"
.include "display.asm"

.eqv GAME_TICK_MS	16
.eqv MAX_BULLETS	10
.eqv NUM_ENEMIES	20



.data
# don't get rid of these, they're used by wait_for_next_frame.
last_frame_time:  .word 0
frame_counter:    .word 0
next_bullet_frame: .word 0
next_movement_frame: .word 0
enemy_bullet_frame: .word 30
inv_frame: .word 0

bullet_x:	.byte 0:MAX_BULLETS
bullet_y:	.byte 0:MAX_BULLETS
bullet_active:	.byte 0:MAX_BULLETS

enemy_x: .word 3
enemy_y: .word 3
enemy_dir: .word 0
enemy_active:	.byte 1:NUM_ENEMIES
enemy_reminaing: .word NUM_ENEMIES

enemy_bullet_active: .byte 0:MAX_BULLETS
enemy_bullet_y: .byte 0:MAX_BULLETS
enemy_bullet_x: .byte 0:MAX_BULLETS

ship_inv: .word 0

ship_image: .byte
 0   0   4   0   0
 0   4   7   4   0
 4   7   7   7   4
 4   4   4   4   4
 4   0   2   0   4

heart_image: .byte
 0   1   0   1   0
 1   1   1   1   1
 1   1   1   1   1
 0   1   1   1   0
 0   0   1   0   0

enemy_image: .byte
 1   0   2   0   1
 1   1   1   1   1
 1   3   3   3   1
 0   1   3   1   0
 0   0   1   0   0

 ship_image_inv: .byte
 0   0   5   0   0
 0   5   7   5   0
 5   7   7   7   5
 5   5   5   5   5
 5   0   2   0   5

ship_x:	.word 32
ship_y: .word 49

bullets: .word 50
lives: .word 3

welcome_message_1: .asciiz "Welcome!"
welcome_message_2: .asciiz "Press the"
welcome_message_3: .asciiz "B key"
welcome_message_4: .asciiz "to begin!"

exit_message_1: .asciiz "Game Over!"
exit_message_2: .asciiz "You Lost!"
exit_message_3: .asciiz "You WON!"

game_won: .word 0


.text

# --------------------------------------------------------------------------------------------------

.globl main
main:
	# set up anything you need to here,
	# and wait for the user to press a key to start.

	li a0, 10
	li a1, 10
	la a2, welcome_message_1
	jal display_draw_text

	li a0, 5
	li a1, 30
	la a2, welcome_message_2
	jal display_draw_text

	li a0, 15
	li a1, 40
	la a2, welcome_message_3
	jal display_draw_text

	li a0, 5
	li a1, 50
	la a2, welcome_message_4
	jal display_draw_text

	jal	display_update_and_clear

	wait_for_key:

	jal input_get_keys
	beq v0, 0x00000000, wait_for_key

	#jal draw_ship

_main_loop:
	# check for input,
	# update everything,
	# then draw everything.

	jal check_input

	jal move_bullets
	jal move_enemies
	jal move_enemy_bullet

	jal draw_ship
	jal draw_lives
	jal draw_bullet_score
	jal draw_bullets
	jal draw_enemies
	jal draw_enemy_bullets

	jal check_game_status


	jal	display_update_and_clear
	jal	wait_for_next_frame
	b	_main_loop

_game_over:
	jal display_update_and_clear
	li a0, 5
	li a1, 30
	la a2, exit_message_1
	jal display_draw_text
	lw t0, game_won
	beq t0, 1, _win_game
	_lose_game:
		li a0, 5
		li a1, 40
		la a2, exit_message_2
		jal display_draw_text
		jal display_update_and_clear
		print_char 'l'
		exit
	_win_game:
		li a0, 10
		li a1, 40
		la a2, exit_message_3
		jal display_draw_text
		jal display_update_and_clear
		print_char 'w'

	exit

# --------------------------------------------------------------------------------------------------
# call once per main loop to keep the game running at 60FPS.
# if your code is too slow (longer than 16ms per frame), the framerate will drop.
# otherwise, this will account for different lengths of processing per frame.

wait_for_next_frame:
enter	s0
	lw	s0, last_frame_time
_wait_next_frame_loop:
	# while (sys_time() - last_frame_time) < GAME_TICK_MS {}
	li	v0, 30
	syscall # why does this return a value in a0 instead of v0????????????
	sub	t1, a0, s0
	bltu	t1, GAME_TICK_MS, _wait_next_frame_loop

	# save the time
	sw	a0, last_frame_time

	# frame_counter++
	lw	t0, frame_counter
	inc	t0
	sw	t0, frame_counter
leave	s0

# --------------------------------------------------------------------------------------------------

# .....and here's where all the rest of your code goes :D

check_game_status:
enter

	lw t0, bullets
	ble t0, 0, _end_game
	lw t0, lives
	ble t0, 0, _end_game
	lw t0, enemy_y
	bge t0, 17, _end_game
	lw t0, enemy_reminaing
	ble t0, 0, _end_game_victory
	jal _continue_game

	_end_game_victory:
		lw t0, 1
		sw t0, game_won
		lw t1, game_won
	_end_game:
		jal _game_over
	_continue_game:

leave

draw_ship:
enter

	lw a0, ship_x
	lw a1, ship_y

	lw t1, ship_inv

	beq t1, 1, _draw_inv_ship
	la a2, ship_image
	b _draw_ship_end

	_draw_inv_ship:
		la a2, ship_image_inv

	_draw_ship_end:
		jal display_blit_5x5


leave

draw_bullet_score:
enter

	li a0, 1
	li a1, 58
	lw a2, bullets
	jal display_draw_int

leave

draw_lives:
enter s0, s1

	lw s0, lives
	li s1, 0

	lives_loop:
		li a0, 58
		add a0, a0, s1
		li a1, 58
		la a2, heart_image
		jal display_blit_5x5
		sub s1, s1, 6
		dec s0
		bgt s0, 0, lives_loop

	exit:

leave s0, s1

check_input:
enter s0, s1

	jal input_get_keys
	lw s0, ship_y
	lw s1, ship_x


	move_up:
		and t0, v0, 1
		bne t0, 1, move_down
		beq s0, 46, move_down
		dec s0
		sw s0, ship_y

	move_down:
		and t0, v0, 2
		bne t0, 2, move_left
		beq s0, 52, move_left
		inc s0
		sw s0, ship_y

	move_left:
		and t0, v0, 4
		bne t0, 4, move_right
		beq s1, 2, move_right
		dec s1
		sw s1, ship_x

	move_right:
		and t0, v0, 8
		bne t0, 8, shoot
		beq s1, 57, shoot
		inc s1
		sw s1, ship_x
	shoot:
		and t0, v0, 16
		bne t0, 16, exit_movement
		jal create_bullet
	exit_movement:

leave s0, s1


draw_bullets:
enter s0

	li s0, 0
	_draw_bullets_loop:
		beq s0, 10, _draw_bullets_exit
		lbu t1, bullet_active(s0)
		beq t1, 0, _draw_bullets_inc
		lbu a0, bullet_x(s0)
		lbu a1, bullet_y(s0)
		li a2, COLOR_WHITE


		jal display_set_pixel

		_draw_bullets_inc:
			inc s0

			b _draw_bullets_loop

	_draw_bullets_exit:

leave s0

create_bullet:
enter s1, s2, s3

	jal search_bullet_array

	beq v0 10 exit_bullet_loop

	lw s1, frame_counter
	lw s2, next_bullet_frame
	bgt s2, s1, exit_bullet_loop

	move s3 v0
	make_bullet:
		li t0, 1
		sb t0, bullet_active(s3)

		lw t1, bullets
		dec t1
		sw t1, bullets

		lw a0, ship_x
		lw a1, ship_y

		add a0, a0, 2
		sub a1, a1, 1


		sb a0, bullet_x(s3)
		sb a1, bullet_y(s3)


		add s1, s1, 15
		sw s1, next_bullet_frame
	exit_bullet_loop:

leave s1, s2, s3

move_bullets:
enter s0, s1

	li s0, 0
	_move_bullets_loop:
		bgt s0, 9, _move_bullets_loop_end
		lbu s1, bullet_active(s0)
		beq s1, 0, _move_bullets_inc

		lbu s1, bullet_y(s0)
		sub s1, s1, 1

		lbu a0, bullet_x(s0)
		move a1, s1
		jal check_collisions

		beq v0, 1, _remove_bullet
		blt s1, 0, _remove_bullet

		sb s1, bullet_y(s0)
		b _move_bullets_inc

	_remove_bullet:
		sb zero, bullet_active(s0)

	_move_bullets_inc:
		inc s0
		b _move_bullets_loop

	_move_bullets_loop_end:

leave s0, s1

search_bullet_array:
enter s0, s1

	li s1, 0
	_search_bullet_loop:
		lb t0, bullet_active(s1)
		beq t0, 0, _search_bullet_loop_found
		inc s1
		blt s1, 10, _search_bullet_loop
	_search_bullet_loop_found:
		move v0, s1
	_search_bullet_loop_end:


leave s0, s1

draw_enemies:
enter s0, s1, s2, s3, s4, s5


	li s0, 0
	li s1, 0
	lw s3, enemy_x
	lw s4, enemy_y

	lw s5, frame_counter


	_enemy_start_1:
		beq s1, 4, _enemy_end_1
		li s2, 0

	_enemy_start_2:
		beq s2, 5, _enemy_end_2
  		lb t0, enemy_active(s0)

  		beq t0, 0, _enemy_inactive

  		move a0, s3
  		move a1, s4
  		la a2, enemy_image

  		jal display_blit_5x5

		li a1, 500
		li v0, 42
		syscall

		lw t1, enemy_bullet_frame
		bgt t1, s5, _enemy_inactive
		bne a0, 1, _enemy_inactive

		move a0, s3
		move a1, s4



		jal create_enemy_bullet

		add s5, s5, 30
		sw s5, enemy_bullet_frame

  		_enemy_inactive:
  			inc s2
  			inc s0
  			add s3, s3, 10
  			b _enemy_start_2
	_enemy_end_2:

		inc s1
		lw s3, enemy_x
		add s4, s4, 8
  		b _enemy_start_1
	_enemy_end_1:


leave s0, s1, s2, s3, s4, s5

move_enemies:
enter s0

	lw s0, frame_counter
	lw t1, next_movement_frame
	bgt t1, s0, _move_enemies_end

	jal check_direction
	beq v0, 1, _move_enemies_end

	lw t0, enemy_x
	lw t1, enemy_dir
	beq t1, 1, _move_left
	beq t1, 0, _move_right

	_move_left:
		dec t0
		b _increment_frame

	_move_right:
		inc t0

	_increment_frame:
		add s0, s0, 20
		sw s0, next_movement_frame
		sw t0, enemy_x
	_move_enemies_end:
		li v0, 0

leave s0


check_direction:
enter s0
	lw t0, enemy_x
	move t6, t0
	move t0, t6
	blt t0, 3, _set_dir_right
	bgt t0, 16, _set_dir_left
	b _set_dir_end

	_set_dir_right:
		li t1, 0
		b _move_down

	_set_dir_left:
		li t1, 1

	_move_down:
		lw t0, enemy_y
		inc t0
		sw t0, enemy_y
		sw t1, enemy_dir

	_set_dir_end:

leave s0

check_collisions:
enter s0, s1, s2, s3, s4, s5
push s6


	li s0, 0
	li s1, 0
	lw s3, enemy_x
	lw s4, enemy_y
	move s5, a0
	move s6, a1
	_collision_start_1:
		beq s1, 4, _collision_end_1
		li s2, 0


	_collision_start_2:
		beq s2, 5, _collision_end_2

		lb t0, enemy_active(s0)
  		beq t0, 0, _enemy_inactive_2

		move a0, s5
		move a1, s6
  		move a2, s3
  		move a3, s4

  		jal check_in_hitbox

		bne v0, 1, _enemy_inactive_2
  		li t5, 0
  		sb t5, enemy_active(s0)
		lw t6, enemy_reminaing
		dec t6
		sw t6, enemy_reminaing
  		li v0, 1
		b _collision_end

  		_enemy_inactive_2:
  			inc s2
  			inc s0
  			add s3, s3, 10
  			b _collision_start_2
	_collision_end_2:

		inc s1
		lw s3, enemy_x
		add s4, s4, 8
  		b _collision_start_1

	_collision_end_1:
		li v0, 0
	_collision_end:



pop s6
leave s0, s1, s2, s3, s4, s5

check_in_hitbox:
enter
		add t0, a2, 4
  		add t1, a3, 4

		move t6, a0
		move t7, a1


  		blt a0, a2, _not_in_box
  		bgt a0, t0, _not_in_box
  		blt a1, a3, _not_in_box
  		bgt a1, t1, _not_in_box
  		li v0, 1
		b _end_check_in_hitbox
  		_not_in_box:
			li v0, 0
		_end_check_in_hitbox:

leave

create_enemy_bullet:
enter s3

	jal search_e_bullet_array

	beq v0 10 exit_e_bullet_loop

	move s3 v0
	make_e_bullet:
		li t0, 1
		sb t0, enemy_bullet_active(s3)

	 	add a0, a0, 2
		add a1, a1, 4


		sb a0, enemy_bullet_x(s3)
		sb a1, enemy_bullet_y(s3)

	exit_e_bullet_loop:

leave s3

search_e_bullet_array:
enter s0, s1

	li s1, 0
	_search_e_bullet_loop:
		lb t0, enemy_bullet_active(s1)
		beq t0, 0, _search_e_bullet_loop_found
		inc s1
		blt s1, 10, _search_e_bullet_loop
	_search_e_bullet_loop_found:
		move v0, s1
	_search_e_bullet_loop_end:


leave s0, s1

move_enemy_bullet:
enter s0, s1

	li s0, 0
	_move_e_bullets_loop:
		bgt s0, 9, _move_e_bullets_loop_end
		lbu s1, enemy_bullet_active(s0)
		beq s1, 0, _move_e_bullets_inc

		lbu s1, enemy_bullet_y(s0)
		add s1, s1, 1

		lbu a0, enemy_bullet_x(s0)
		move a1, s1
		jal check_enemy_collisions

		beq v0, 1, _remove_e_bullet
		bgt s1, 63, _remove_e_bullet

		sb s1, enemy_bullet_y(s0)
		b _move_e_bullets_inc

	_remove_e_bullet:
		sb zero, enemy_bullet_active(s0)

	_move_e_bullets_inc:
		inc s0
		b _move_e_bullets_loop

	_move_e_bullets_loop_end:

leave s0, s1

draw_enemy_bullets:
enter s0

	li s0, 0
	_draw_e_bullets_loop:
		beq s0, 10, _draw_e_bullets_exit
		lbu t1, enemy_bullet_active(s0)
		beq t1, 0, _draw_e_bullets_inc
		lbu a0, enemy_bullet_x(s0)
		lbu a1, enemy_bullet_y(s0)
		li a2, COLOR_WHITE


		jal display_set_pixel

		_draw_e_bullets_inc:
			inc s0

			b _draw_e_bullets_loop

	_draw_e_bullets_exit:

leave s0

check_enemy_collisions:
enter s0, s1

	lw s0, frame_counter
	lw s1, inv_frame
	bgt s1, s0, _make_inv

	li t0, 0
	sw t0, ship_inv

	lw a2, ship_x
	lw a3, ship_y
	jal check_in_hitbox
	beq v0, 0, _player_not_hit

	li v0, 1
	lw t1, lives
	dec t1
	sw t1, lives
	add s0, s0, 90
	sw s0, inv_frame

	_make_inv:
		li t0, 1
		sw t0, ship_inv

	_player_not_hit:

leave s0, s1
