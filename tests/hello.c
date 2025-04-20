#include <stdio.h>
#include <riscv-pk/encoding.h>
#include "marchid.h"
#include <stdint.h>

#include "accel_blake2s.h"
#include "accel_chacha.h"

int main(void) {
  uint64_t marchid = read_csr(marchid);
  const char* march = get_march(marchid);
  printf("Hello world from core 0, a %s\r\n", march);

  blake2s_test_empty_message();
  blake2s_test_RFC_7693();

  printf("\n--------------------------------------\n");
  chacha_soft_reset();
  printf("ChaCha NAME0: 0x%08x\n", chacha_read_from_address(0x00));
  printf("ChaCha NAME1: 0x%08x\n", chacha_read_from_address(0x01));
  printf("ChaCha INFO:  0x%08x\n\n", chacha_read_from_address(0x02));
  chacha_test_cases();

  return 0;
}
