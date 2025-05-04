// #include <stdio.h>
#include <stdint.h>
#include <riscv-pk/encoding.h>
#include "platform.h"
#include "kprintf.h"

#include "mmio.h"

#include "cipher/accel_ascon.h"
#include "cipher/accel_chacha.h"
#include "cipher/accel_blake2s.h"

#define REG32(p, i)	((p)[(i) >> 2])

int main(int hartid, char **arv) {

  if(hartid == 0)
    kprintf("Hello world!\r\n");

  ascon_test();
  blake2s_test_RFC_7693();
  chacha_test_cases();

  return 0;
}
