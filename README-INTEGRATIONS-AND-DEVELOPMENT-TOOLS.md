###### Escola: FIAP
###### Curso: MBA FULLSTACK DEVELOPER, MICROSERVICES, CLOUD & IoT
###### Aluno / RM: JEAN BRUNO SOUTO VILLETE / 335435
###### Prof.: CARLOS VINICIUS MAGNABOSCHI HESKETH

# 1 - INTEGRATIONS & DEVELOPMENT TOOLS

O projeto em questão atende ao trabalho para avaliação da disciplina **Integration and Development Tools**, que
 compreende o desenvolvimento de um aplicativo desenvolvido em módulos que se comunicam assincronamente através do 
 protocolo AMQP, mais especificamente com o message broker RabbitMQ.

O repositório corrente é uma ideia fictícia do gerenciamento de um estoque e a apresentação dos produtos do estoque em
 um portal, onde há a implementação de microserviços que atendem o estoque com seu backend e frontend específico, e
 outra implementação de microserviços que atendem o portal, também com seu backend e frontend específicos.
 
A cada disciplina/curso dentro do MBA, o conhecimento é aplicado no repositório a fim de agregar no projeto.
 
***A integração entre os dois módulos até este ponto era feita sincronamente, através de requisições RESTFul feitas entre
 eles.***

Logo para o curso corrente agregou em um melhor (mais baixo) acoplamento entre os módulos e a comunicação assincrina
 aplicada através do RabbitMQ, utilizando exchanges (fanout) e filas duráveis, além da gerência manual de 
 **acknowledgments**.