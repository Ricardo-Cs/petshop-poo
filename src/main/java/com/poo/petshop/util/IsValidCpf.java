package com.poo.petshop.util;

public class IsValidCpf {
    public static boolean check(String cpf) {
        return cpf != null && cpf.matches("\\d{11}");
    }
}