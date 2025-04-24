// #include <stdio.h>
#include <stdint.h>
#include <riscv-pk/encoding.h>
#include "platform.h"
#include "kprintf.h"

//#include "cipher/accel_chacha.h"
//#include "cipher/accel_sha3.h"
//#include "cipher/accel_aes.h"
//#include "cipher/accel_klein.h"
//#include "cipher/accel_ascon.h"

#define REG32(p, i)	((p)[(i) >> 2])

int main(int hartid, char **arv) {

  if(hartid == 0) {
    // chacha core test
//    chacha_soft_reset();
//    chacha_test_cases();

    // sha3-512 core test
//    sha3_soft_reset();
//    sha3_test_cases();

    // aes core test
//    aes_test_cases();

    // klein
//    klein_test();
//    klein_test_encryption_elapsed();
//    klein_test_decryption_elapsed();
//    klein_test_software();

    // ascon
//  ascon_test();
  kprintf("Hello world!\r\n");

  }
  return 0;
}
