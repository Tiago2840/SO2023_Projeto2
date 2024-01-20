# Sistemas Operativos 2023 / 2024

## Licenciatura em Engenharia Informática

### Trabalho Prático #2

#### Docentes: Aníbal Ponte e Miguel Bugalho

---
O ficheiro tsp2.jar encontra-se na pasta **SO2023_Projeto2\out\artifacts\SO2023_Projeto2_jar\tsp2.jar**

<div><br>
    <b>Trabalho prático desenvolvido por:</b>
    <ol>
        <li><a href="https://github.com/laranjeira15">João Espada (202100660)</a></li>
        <li><a href="https://github.com/Tiago2840">Tiago Silva (202000331)</a></li>
    </ol>
</div>

## TOC

1. [Análise dos resultados obtidos](#Análise-de-resultados)
2. [Resultados obtidos nos testes realizados](#Resultados-de-execução)

## Análise de resultados

Todos os algoritmos foram capazes de encontrar as soluções perfeitas em todos os testes executados até ao ficheiro gr17.
<br><br>A partir do ulysses22 ambas as versões BaseVersion e AdvancedVersion começaram a ter maiores dificuldades para
encontrar o melhor caminho possível.

- AdvancedVersion: 2/10
- BaseVersion: 5/10
- OriginalVersion: 10/10

<br>No ficheiro gr24 surgiram as primeiras dificuldades para a OriginalVersion e acentuaram-se as da Base e Advanced.

- AdvancedVersion: 2/10
- BaseVersion: 0/10
- OriginalVersion: 8/10

<br> Doravante o ficheiro fri26 todos os algoritmos tiveram grandes dificuldades para alcançar o melhor caminho possível.

- AdvancedVersion: 2/10
- BaseVersion: 1/10
- OriginalVersion: 2/10

<br> Para as 42 e 48 cidades, os algoritmos não foram capazes de determinar o melhor caminho através dos parâmetros passados nos testes. No entanto, é possível verificar, através dos resultados de execução, que no geral a OriginalVersion foi a que esteve sempre mais perto do resultado ideal e, sempre com menos iterações para o mesmo limite de tempo.
<br> Concluí-se assim que o melhor e mais eficiente algoritmo é o Original, seguido do Advanced e por último o Base.

## Resultados de execução

### uk12

- **AdvancedVersion:**
    - Best result found: 10/10
    - Average total iterations: 11,422,711.90
- **BaseVersion:**
    - Best result found: 10/10
    - Average total iterations: 2,024,900.30
- **OriginalVersion:**
    - Best result found: 10/10
    - Average total iterations: 405,795.30

---

### ex13

- **AdvancedVersion:**
    - Best result found: 10/10
    - Average total iterations: 15,377,737.20
- **BaseVersion:**
    - Best result found: 10/10
    - Average total iterations: 3,115,584.60
- **OriginalVersion:**
    - Best result found: 10/10
    - Average total iterations: 785,544.70

---

### burma14

- **AdvancedVersion:**
    - Best result found: 10/10
    - Average total iterations: 71,193,393.10
- **BaseVersion:**
    - Best result found: 10/10
    - Average total iterations: 14,528,977.30
- **OriginalVersion:**
    - Best result found: 10/10
    - Average total iterations: 941,391.30

---

### lau15

- **AdvancedVersion:**
    - Best result found: 10/10
    - Average total iterations: 37,458,921.00
- **BaseVersion:**
    - Best result found: 10/10
    - Average total iterations: 6,688,935.30
- **OriginalVersion:**
    - Best result found: 10/10
    - Average total iterations: 809,927.20

---

### ulysses16

- **AdvancedVersion:**
    - Best result found: 10/10
    - Average total iterations: 29,262,165.20
- **BaseVersion:**
    - Best result found: 10/10
    - Average total iterations: 5,387,328.50
- **OriginalVersion:**
    - Best result found: 10/10
    - Average total iterations: 1,139,386.80

---

### gr17

- **AdvancedVersion:**
    - Best result found: 10/10
    - Average total iterations: 26,645,934.60
- **BaseVersion:**
    - Best result found: 10/10
    - Average total iterations: 4,770,261.10
- **OriginalVersion:**
    - Best result found: 10/10
    - Average total iterations: 1,136,661.20

---

### ulysses22

- **AdvancedVersion:**
    - Best result found: 2/10
    - Average total iterations: 42,758,489.10
- **BaseVersion:**
    - Best result found: 5/10
    - Average total iterations: 7,031,289.00
- **OriginalVersion:**
    - Best result found: 10/10
    - Average total iterations: 1,985,132.90

---

### gr24

- **AdvancedVersion:**
    - Best result found: 2/10
    - Average total iterations: 38,057,097.30
- **BaseVersion:**
    - Best result found: 0/10
    - Average total iterations: 6,866,435.30
- **OriginalVersion:**
    - Best result found: 8/10
    - Average total iterations: 2,260,347.10

---

### fri26

- **AdvancedVersion:**
    - Best result found: 2/10
    - Average total iterations: 147,121,092.30
- **BaseVersion:**
    - Best result found: 1/10
    - Average total iterations: 26,003,652.10
- **OriginalVersion:**
    - Best result found: 2/10
    - Average total iterations: 6,919,777.90

---

### dantzig42

- **AdvancedVersion:**
    - Best result found: 0/10
- **BaseVersion:**
    - Best result found: 0/10
- **OriginalVersion:**
    - Best result found: 0/10

---

### att48

- **AdvancedVersion:**
    - Best result found: 0/10
- **BaseVersion:**
    - Best result found: 0/10
- **OriginalVersion:**
    - Best result found: 0/10
