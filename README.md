# auth-server

A implementação desse microsserviço é para dar apoio ao microsserviço [**rotten-itaumatoes**](https://github.com/trepudox/rotten-itaumatoes).  

O **auth-server** tem como funções principais o cadastro, a autenticação e autorização do usuário, sendo concedidas as devidas permissões após seu login.

A aplicação dispõe de um Swagger, que pode ser acessado através do endereço: http://localhost:8081/swagger-ui/

Além do Swagger, temos uma pasta no drive com a [collection do Postman](https://drive.google.com/drive/folders/1GNoDN1rn2h7-BfvoyHFqLzxkWXj7_jbW) e alguns outros recursos, para testar o auth-server e o rotten-itaumatoes.  

Mas lembrando, ao realizar o cadastro diretamente pelo auth-server, a senha não será criptografada antes da persistência no banco de dados, então o login através do rotten-itaumatoes usando esse usuário não funcionará.

## Como rodar o auth-server

No ambiente local foram utilizados Java 11, e as últimas versões das imagens do MySQL e Redis no Docker.

**IMPORTANTE!** 

Na documentação do [**rotten-itaumatoes**](https://github.com/trepudox/rotten-itaumatoes) citamos que a criação das tabelas e do banco de dados é feita pela própria aplicação.  

O auth-server consome alguns dados da base que o rotten-itaumatoes cria, então antes de usar essa aplicação, dê ao menos um Run no rotten-itaumatoes.

Após isso, nossas tabelas estarão criadas e prontas para uso! E então é só continuar seguindo a documentação!

Bom, além desse passo, precisamos ter uma instância do MySQL e uma instância do Redis, ambas rodando simultâneamente.  

A conexão do Redis e do MySQL podem ser alteradas pelo arquivo **application.yml**.  

Para utilizar o MySQL e o Redis localmente e nas portas padrão (**3306** para MySQL e **6379** para Redis), a maneira mais simples é usando o Docker.  

## Usando o Docker para rodar o MySQL e o Redis

Com o Docker rodando, a primeira coisa que devemos fazer, é baixar as imagens do Docker Hub para a nossa máquina.

Usando o PowerShell (ou qualquer outro console que te atenda) rode os seguintes comandos:

**Redis**:
``
docker pull redis
``

**MySQL**:
``
docker pull mysql
``

Dessa maneira, já temos as imagens do Redis e do MySQL para criarmos os contâiners necessários para nossas aplicações.  

### Container do MySQL

Para criar um container do MySQL, devemos rodar o seguinte comando:

``
docker run -e MYSQL_ROOT_PASSWORD=root -dp 3306:3306 -it mysql
``

Onde temos algumas flags, que são importantes dependendo do que queremos.

A flag `-e` serve para definir uma variável de ambiente, então estamos definindo que a senha *root* do MySQL no container será root.

Já a flag `-dp` são duas flags acopladas, a `--detach` e a `--port`, usando `--detach` o container irá rodar em background, já a `--port` é para mapearmos as portas.

A última flag, `-it`, é para tornar o container interativo, ou seja, conseguiremos acessar os arquivos do container, a bash e outros recursos dentro dele.

Após executar o `docker run`, o Docker criará um container e imprimirá 4 caracteres no console.

Esses caracteres são o começo do **container ID**, precisamos dele para identificar o container.

Com o container ID em mãos, podemos interagir com o container (lembra da flag `-it`?).

Então, rodando o seguinte comando conseguimos acessar o MySQL que está em execução dentro do container:

``
docker exec -it <containerID> mysql -uroot -p
``

Onde estamos informando que logaremos através do user **root** (pela flag `-uroot`) e que estaremos informando a senha para acessá-lo (pela flag `-p`).

Só não se esqueça de alterar o `<containerID>` para o ID do seu container MySQL!

Dessa maneira já conseguimos acessar o MySQL do container e executar instruções dentro dele!

### Container do Redis

A criação do container do Redis é semelhante ao do MySQL, porém um pouco mais simples.  

Para a criação, executaremos o seguinte `docker run`:  

``
docker run -dp 6379:6379 -it redis
``

E após pegarmos o container ID, devemos dessa vez acessar o console do container, através desse comando:  

``
docker exec -it <containerID> bash
``

Agora, no console do container, executando o comando `redis-cli` estaremos entrando no console do Redis.  