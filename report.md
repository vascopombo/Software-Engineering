100 Reads

O teste dos 100 Reads consistia em ler as várias informações da base de dados em ciclos de 100 por cada utilizador, os testes foram executados para 2000 utilizadores.
Pela análise dos testes podemos constatar ter uma percentagem de erros de leitura em média de 1,5%, erros esses que podem ter resultado da baixa população de serviços criados para os testes, ou também de demasiados acessos em simultâneo ao mesmo conteúdo da base de dados.
Este tipo de testes em comparação com os testes de Junit executados no inicio do projecto tem vantagens e desvantagens. Como vantagem tem o facto de permitirem testar como se comporta o programa quando tem de criar múltiplos serviços em simultâneo, por exemplo criar bancos, actividades,hotéis, etc,.
Permite também perceber como o sistema se comporta quando tem de ler muita informação da base de dados ao mesmo tempo, e por parte de múltiplos utilizadores.
As desvantagens destes testes é que não permitem perceber problemas mais pequenos de cada um dos métodos usados, perceber se as condições estão a ser executadas e se o código faz o que se pretende, para este tipo de testes é melhor o Junit.

30Writes

O teste dos 30Writes consistia em ler as várias informações da base de dados e executar três process em ciclos de 30 por cada utilizador, os testes foram executados para 2000 utilizadores.
Pela análise dos testes podemos constatar ter uma percentagem de erros em média de 8%, erros esses que se concentram com maior percentagem nos process (16%). Existiu uma maior percentagem de falhas no read do adventure provavelmente por pedirmos um loop de 100 adventure mas só temos informação para 20 diferentes.
Este tipo de testes em comparação com os testes de Junit executados no inicio do projecto tem vantagens e desvantagens. Como vantagem tem o facto de permitirem testar como se comporta o programa quando tem de criar múltiplos serviços em simultâneo, por exemplo criar bancos, actividades,hotéis, etc,.
Permite também perceber como o sistema se comporta quando tem de ler muita informação da base de dados ao mesmo tempo, e por parte de múltiplos utilizadores.
As desvantagens destes testes é que não permitem perceber problemas mais pequenos de cada um dos métodos usados, perceber se as condições estão a ser executadas e se o código faz o que se pretende, para este tipo de testes é melhor o Junit.

100Writes

O teste dos 100Writes consistia em executar 5 process em ciclos de 100 por cada utilizador, os testes foram executados para 100 utilizadores.
Pela análise dos testes podemos constatar ter uma percentagem de erros de leitura em média de 1.6 %, erros esses que apenas existem nos process, esta percentagem pode resultar de demasiados acessos em simultâneo ao mesmo conteúdo da base de dados, se bem que a descrepância entre o resultado do 30writes e do 100writes com a mesma população nos deixa ou pouco confusos.
Este tipo de testes em comparação com os testes de Junit executados no inicio do projecto tem vantagens e desvantagens. Como vantagem tem o facto de permitirem testar como se comporta o programa quando tem de criar múltiplos serviços em simultâneo, por exemplo criar bancos, actividades,hotéis, etc,.
Permite também perceber como o sistema se comporta quando tem de ler muita informação da base de dados ao mesmo tempo, e por parte de múltiplos utilizadores.
As desvantagens destes testes é que não permitem perceber problemas mais pequenos de cada um dos métodos usados, perceber se as condições estão a ser executadas e se o código faz o que se pretende, para este tipo de testes é melhor o Junit.
