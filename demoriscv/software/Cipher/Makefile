#################################
# RISCV Toolchain
#################################

ROOT_DIR := $(dir $(abspath $(lastword $(MAKEFILE_LIST))))
BUILD_DIR := $(ROOT_DIR)build

CC=$(RISCV)/bin/riscv64-unknown-elf-gcc
OBJCOPY=$(RISCV)/bin/riscv64-unknown-elf-objcopy
OBJDUMP=$(RISCV)/bin/riscv64-unknown-elf-objdump

CFLAGS=-march=rv64gc -mcmodel=medany -O0 -std=gnu11 -Wall -nostartfiles
CFLAGS+= -fno-common -g -DENTROPY=0 -mabi=lp64d -DNONSMP_HART=0
CFLAGS+= -I $(ROOT_DIR)/include -I.

LFLAGS=-static -nostdlib -L $(ROOT_DIR)/linker -T hello.elf.lds

# Clock option passed in
PBUS_CLK ?= 1000000

#################################
# Build cipher by default
#################################
.PHONY: default
default: cipher.bin cipher.dump

#################################
# CIPHER build rules
#################################
CIPHER_ELF  := $(BUILD_DIR)/cipher.elf
CIPHER_BIN  := $(BUILD_DIR)/cipher.bin
CIPHER_DUMP := $(BUILD_DIR)/cipher.dump

$(CIPHER_ELF): cipher.c kprintf.c
	mkdir -p $(BUILD_DIR)
	$(CC) $(CFLAGS) -DTL_CLK="$(PBUS_CLK)UL" $(LFLAGS) -o $@ crt0.S cipher.c kprintf.c

.PHONY: cipher.elf
cipher.elf: $(CIPHER_ELF)

$(CIPHER_BIN): $(CIPHER_ELF)
	mkdir -p $(BUILD_DIR)
	$(OBJCOPY) -O binary $< $@

.PHONY: cipher.bin
cipher.bin: $(CIPHER_BIN)

$(CIPHER_DUMP): $(CIPHER_ELF)
	$(OBJDUMP) -D -S $< > $@

.PHONY: cipher.dump
cipher.dump: $(CIPHER_DUMP)

#################################
# CLEAN
#################################
.PHONY: clean
clean:
	rm -rf $(BUILD_DIR)
