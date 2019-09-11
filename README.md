# LISTA 2 - PROJETO DE ALGORITMOS - 2019/2

### Henrique Martins de Messias - 17/0050394

<br>

### Instalações necessárias
- Seu dispositivo deve ter o Java instalado


### Instruções de uso

- No terminal, vá até o diretório do exercício, que contém, além de arquivos como o README, a pasta "src"
- Digite o seguinte comando:

  ```bash
    $ cd src
  ```

  - Em seguida, digite:

  ```bash
    $ javac Main.java
  ```

- Para executar o código, digite:

  ```bash
    $ java Main
  ```

### Detalhes da Lista 2

O software deste repositório envolve calcular a <b>menor distância entre hospitais e acidentes</b>.

O propósito desse programa é que o usuário defina áreas onde serão os hospitais, um local onde haverá um acidente e as rotas disponíveis no mapa, com as suas respectivas distâncias. O programa irá então calcular o menor trajeto possível que uma ambulância pode fazer de um hospital até o acidente e, em seguida, do acidente até o hospital. A ambulância não tem a necessidade de voltar para o hospital de onde veio (pode ser que isso nem seja possível). Paraque o cálculo seja feito, deve haver pelo menos um hospital e um acidente. Se não for possível chegar até o acidente ou até o hospital, o programa dirá.

Ao executar o programa, uma tela com um mapa se abrirá. O mapa cobre a maior parte da tela, sendo os círculos numerados áreas disponíveis do mapa.

Embaixo do mapa, o usuário verá os seguintes botões
  - Criar Hospital (o usuário deverá clicar, então, nas áreas onde ele quer que sejam os hospitais)
  - Apagar Local (o usuário deverá clicar, então, nas áreas onde ele quer que sejam apagadas. Áreas apagadas são inacessíveis, ou seja, não pode haver um caminho para ela)
  - Adicionar Local (o usuário deverá clicar, então, nas áreas apagadas que ele deseja que se tornem acessíveis de novo)
  - Criar Distância (o usuário deverá clicar, então, em duas áreas e digitar um número entre 1 e 9. Esse número será a distância entre essas áreas. A distância definida é em apenas uma direção. Se o usuário quiser que ida e volta também seja possível, deverá clicar nas duas áreas de novo, mas na ordem inversa)
  - Apagar Distância (o usuário deverá clicar, então, nas duas áreas que ele deseja deletar a distância. Caso seja possível fazer o percursso nas duas direções, a outra ainda existirá)
  - Acidente (o usuário deverá clicar, então, na área que ele deseja que seja o acidente)
  - Traçar rota (se todas as condições forem atendidas, o programa irá calcular a menor rota, se existir)

Ao lado direito do mapa, há as seguintes informações
  - Início (hospital de origem da ambulância)
  - Fim (hospital de destino da ambulância)
  - Distância Inicial (distância entre o hospital de origem e o acidente)
  - Distância Final (distância entre o acidente e o hospital de destino)
  - Distância Total (distância que a ambulância percorrerá do hospital de origem ao hospital de destino)