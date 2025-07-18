package com.poo.petshop.utils;

public class IsValidCpf {
    public static boolean check(String cpf) {
        return cpf != null && cpf.matches("\\d{11}"); // Validação simples, interessante alterar depois
    }
}