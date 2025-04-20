
    .section .text.init
    .option norvc
    .global atomic_write

// Atomic write
atomic_write:
    addi sp, sp, -8
    sd t0, 0(sp)
    amoswap.w t0, a1, (a0)
    ld t0, 0(sp)
    addi sp, sp, 8
    ret