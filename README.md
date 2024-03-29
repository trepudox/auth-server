# auth-server

A implementação desse microsserviço é para dar apoio ao microsserviço [**rotten-itaumatoes**](https://github.com/trepudox/rotten-itaumatoes).  

O **auth-server** tem como funções principais o cadastro, a autenticação e autorização do usuário, sendo concedidas as devidas permissões após seu login.

A aplicação dispõe de um Swagger, que pode ser acessado através do endereço: http://localhost:8081/swagger-ui/

Além do Swagger, temos uma pasta no drive com a [collection do Postman](https://drive.google.com/file/d/16KqEBpyp60SBjGm1w_oGYgsHSj0kQ3Iv/view?usp=sharing) e alguns outros recursos, para testar o auth-server e o rotten-itaumatoes.  

Mas lembrando, ao realizar o cadastro diretamente pelo auth-server, a senha **não será** criptografada antes da persistência no banco de dados, quem possui a resoponsabilidade de criptografar a senha é o serviço rotten-itaumatoes, então o login através do rotten-itaumatoes usando esse usuário não funcionará.

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

## Tentativas de login no Redis

### O que são as tentativas de login?

Aqui no auth-server, na funcionalidade de login, implementamos uma verificação de tentativas de login.  

Então quando um usuário erra a senha do seu login, ele possui uma quantidade máxima de tentativas, por padrão são 3.

Foi criada uma entidade, a **Login Attempts**, que é responsável por armazenar as tentativas de cada usuário, que tem um Time To Live no Redis de 300 segundos.  

### Como consultá-las

Ao realizar um login com a senha errada pelo rotten-itaumatoes temos a seguinte resposta:  

<p align="center"><img src="img/wrong-password.png" alt="Wrong password login"></p>

Significa que já temos esse registro no Redis e podemos consultá-lo.  

Entrando no console do Redis, podemos ver todas as chaves que temos armazenadas no momento, com o comando `KEYS *`.  

No caso do login acima, temos as seguintes chaves:  

<p align="center"><img src="img/all-keys.png" alt="All stored keys"></p>

Onde a chave `LA` é um Set, e seu valor é o nome de cada usuário. Seus valores podem ser lidos através do comando `SMEMBERS <key>`.  

<p align="center"><img src="img/LA-values.png" alt="LA values"></p>

E a `LA:username` é um Hash, que pode ser lido através do comando `HGETALL <key>`. O retorno deve ser como esse:  

<p align="center"><img src="img/LAusername-value.png" alt="LA:username value"></p>

O que pode parecer um pouco estranho, mas ele serializa a classe Java para poder desserializar ela na aplicação quando necessário.  

Então, a linha 1 indica que é uma classe, a 2 é o nome dela incluindo os pacotes desde a raíz do projeto.  

As linhas 3 e 5 se referem ao nome do atributo na classe **LoginAttemptsModel**, que foi a classe que criamos.  

Já a linha 4 e 6 são os valores do atributo. Linha 4 é o valor do atributo `"currentAttempt"` e linha 6 é o valor do atributo `"username"`.  

E para ver o tempo de vida do registro, é só usar o comando `TTL <key>`, como no exemplo:  

<p align="center"><img src="img/TTL-LAusername.png" alt="Time to Live of LA:username"></p>

## Encerramento

Espero que todas as dúvidas em relação ao projeto estejam sanadas, e que tenha gostado de como ele foi desenvolvido.

Qualquer dúvida, sugestão ou comentário, pode entrar em contato comigo pelo meu LinkedIn ou me mandar um email, deixarei os contatos abaixo. Valeu!

LinkedIn: https://www.linkedin.com/in/marcoa-queiroz/
Emails: teoaa2@gmail.com
marcoa.queiroz1722@gmail.com