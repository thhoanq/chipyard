// See LICENSE.Sifive for license details.
#include <platform.h>
#include <smp.h>
#include "common.h"

    .equ  SCRATCH_PAGE_SIZE,  0x2000  // 8kB
    .equ  STACK_SIZE,         0x800   // 2kB each hart
    .equ  STACK_SHIFT,        11
    .equ  SCRATCH_TOP,        (PAYLOAD_DEST + SCRATCH_PAGE_SIZE)
    .section .text.init
    .option  norvc
    .globl   _prog_start
_prog_start:
    csrr    s0, mhartid
    beqz    s0, _setup
    li      t0, 0x8
    csrw    mie, t0
    wfi
    j       _entry
_setup:
    li      t0, 1
    li      t1, (UART_CTRL_ADDR + UART_REG_TXCTRL)
    sw      t0, 0(t1)
_entry:
    csrw    mie, zero
    csrr    a0, mhartid
    addi    a1, a0, 1
    slli    a1, a1, STACK_SHIFT
    li      t0, SCRATCH_TOP
    sub     sp, t0, a1
    addi    sp, sp, STACK_SIZE-16
    call    main
_loopback:
    li      t0, 0x8
    csrw    mie, t0
    wfi
    j       _loopback

    .size _prog_start, . - _prog_start
