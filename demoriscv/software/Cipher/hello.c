// #include <stdio.h>
#include <stdint.h>
#include <riscv-pk/encoding.h>
#include "include/platform.h"
#include "kprintf.h"

#include "cipher/accel_chacha.h"
#include "cipher/accel_blake2s.h"

#define REG32(p, i)	((p)[(i) >> 2])

int main(int argc, char **arv) {
  
  REG32(uart, UART_REG_TXCTRL) = UART_TXEN;

  uint32_t mhartid = read_csr(mhartid);

  kprintf("Hello world from core %c!!!\r\n", mhartid + 48);

  kprintf("\r\n\r\n\r\n");

  kprintf("     ____  _____ ____  _        _    ____  \r\n");
  kprintf("    |  _ \\| ____/ ___|| |      / \\  | __ ) \r\n");
  kprintf("    | | | |  _| \\___ \\| |     / _ \\ |  _ \\ \r\n");
  kprintf("    | |_| | |___ ___) | |___ / ___ \\| |_) |\r\n");
  kprintf("    |____/|_____|____/|_____/_/   \\_\\____/ \r\n");
  kprintf("\r\n");
  kprintf("  Quad-Core RISC-V 64-bit - A Chipyard project\r\n");
  kprintf("  with NoC and Lightweight Cryptography Accelerators ...\r\n");

  kprintf("\r\nRISC-V program started, hello there!\r\n\r\n");

  // Elapsed start
//  klein_test_encryption_elapsed();
//  klein_test_decryption_elapsed();
//  chacha_test_elapsed();
  // Elapsed end

  // klein core test
//  klein_test();

  // chacha core test
  chacha_soft_reset();
  chacha_test_cases();

  // blake2s core test
  blake2s_clear_block();
  blake2s_test_empty_message();
  blake2s_clear_block();
  blake2s_test_RFC_7693();

  // present core test
//  present_test_cases();

  // dmpresent core test
//  dmpresent_test_cases();

  // prince core test
//  prince_test_cases();

  // sha3-512 core test
//  sha3_test_cases();

  // aes core test
//  aes_test_cases();

  return 0;
}
