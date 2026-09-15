# **DEFINIÇÃO DO ESCOPO** 

## **Projeto: PetVax** 

**Grupo: G** 

### **1. Definição do escopo do sistema – Contextualização** 

O sistema em desenvolvimento para a Vet Home, denominado PetVax, visa facilitar o processo de agendamento, acompanhamento e notificação de vacinação de animais de estimação por meio de um programa desktop desenvolvido em Java. O sistema será utilizado exclusivamente pelos médicos veterinários responsáveis pela empresa, que também são os proprietários da empresa e realizam os atendimentos de forma presencial. 

O PetVax permitirá aos médicos veterinários cadastrar e consultar tutores, seus respectivos Pets e as vacinas disponíveis, além de realizar o agendamento das aplicações e registrar o histórico de vacinação. O tutor não terá acesso direto ao sistema; suas informações serão registradas e administradas pelos veterinários durante ou a partir dos atendimentos presenciais. 

O principal objetivo do PetVax será auxiliar os médicos veterinários no controle das vacinas dos animais, permitindo identificar aplicações realizadas, próximas doses e datas previstas para novas vacinações. A partir dessas informações, o sistema poderá gerar notificações e lembretes para os veterinários, auxiliando no acompanhamento dos Pets e na organização dos atendimentos presenciais. 

O software será instalado nos computadores utilizados pelos médicos veterinários, funcionando de forma independente da web, mas com possibilidade futura de integração com serviços externos de notificação. 

### **2. Relação com outros sistemas** 

Assim como em outros sistemas de gerenciamento veterinário, será necessário manter cadastros de tutores e Pets para registrar e acompanhar suas informações de vacinação. Entretanto, no PetVax, todas as operações serão realizadas pelos próprios médicos veterinários, não havendo um perfil de acesso destinado aos tutores. 

O sistema terá como foco o agendamento presencial das vacinações, o registro das doses aplicadas, o acompanhamento das próximas doses e a emissão de notificações e lembretes para auxiliar os veterinários na rotina. Outros sistemas podem oferecer funcionalidades mais amplas, como consultas, exames, banho e tosa, hospedagem, planos e gestão financeira, mas essas funcionalidades não fazem parte do foco inicial do PetVax. 

### **3. Justificativa** 

O acompanhamento adequado da vacinação dos animais é uma atividade importante na rotina de atendimento veterinário. O uso de registros manuais ou planilhas pode dificultar o controle das datas de vacinação, dos históricos dos Pets e das próximas doses, aumentando a possibilidade de falhas de organização. 

O PetVax busca oferecer uma solução simples e prática para centralizar essas informações e auxiliar os médicos veterinários durante os atendimentos presenciais. Por meio do cadastro, agendamento e registro das vacinas, os profissionais poderão acompanhar a situação de cada Pet e receber notificações sobre vacinações programadas ou próximas do prazo, tornando o processo mais organizado e reduzindo a possibilidade de esquecimento. 

### **4. Macro funções** 

- Cadastro de tutores e Pets: registro dos responsáveis e das informações dos animais pelos médicos veterinários. 

- Cadastro de vacinas: registro dos tipos de vacina, informações básicas e periodicidade. 

- Agendamento de vacinação: definição de data e horário para realização do atendimento presencial. 

- Registro de vacinação: armazenamento das vacinas aplicadas, datas e informações relacionadas ao atendimento. 

- Histórico de vacinação: consulta ao histórico completo de vacinas de cada Pet. 

- Controle de próximas doses: acompanhamento das datas previstas para novas aplicações. 

- Notificações e lembretes: geração de avisos para os médicos veterinários sobre vacinações agendadas e próximas doses. 

- Relatórios básicos: consulta das vacinações realizadas, agendadas e pendentes. 

### **5. Restrições** 

Durante a execução do projeto, diversas limitações podem ocorrer no percurso, afetando seu desenvolvimento até sua conclusão. As principais restrições podem ser: 

- Prazo: atrasos no desenvolvimento podem comprometer a entrega final. 

- Escopo: a inclusão de funcionalidades que não estejam relacionadas ao gerenciamento de vacinação pode aumentar a complexidade do projeto. 

- Custos: limitação a ferramentas gratuitas. 

- Qualidade: limitações de tempo e recursos podem comprometer a experiência do usuário. 

- Funcionamento local: a versão inicial será utilizada presencialmente pelos médicos veterinários e funcionará localmente, podendo limitar integrações com serviços externos. 

### **6. Riscos** 

Durante o desenvolvimento de um projeto, diversos fatores podem influenciar em seu andamento, gerando um progresso mais lento ou até mesmo causando seu cancelamento. O mapeamento dos riscos é importante para identificar situações que possam afetar o desenvolvimento e permitir a busca antecipada por soluções. 

Para o desenvolvimento do software PetVax, os riscos podem ser: 

- Equipamento defeituoso: um computador que apresente falhas pode causar perda de progresso caso não exista uma cópia de segurança, além de gerar atrasos. 

- Descumprimento do prazo de entrega: dificuldades de desenvolvimento ou falta de controle do cronograma podem comprometer a entrega. 

- Dificuldade com a complexidade do projeto: o planejamento inicial pode não prever corretamente a dificuldade de determinadas funcionalidades. 

- Falta de conhecimento técnico: podem ocorrer dificuldades na programação do sistema, no armazenamento dos dados ou na implementação das notificações. 

- Falhas nas notificações: problemas no controle de datas ou na geração dos avisos podem fazer com que lembretes sejam exibidos incorretamente. 

- Cadastro incorreto: informações registradas de forma incorreta pelos usuários do sistema podem prejudicar o acompanhamento do histórico e das próximas vacinações. 

### **7. Stakeholders** 

O quadro abaixo apresenta os stakeholders deste projeto. 

|**Nome**|**Descrição**|**Responsabilidade**<br>**s**|**Stakeholder**|
|---|---|---|---|
|Administrador do<br>sistema|Responsável por<br>acompanhar a<br>disponibilidade do<br>sistema e<br>solucionar questões<br>de infraestrutura.|Manter a<br>infraestrutura do<br>sistema disponível<br>e realizar a<br>manutenção<br>técnica necessária.|Administrador /<br>Gerente de TI|
|Médicos<br>Veterinários|Proprietários da<br>empresa e únicos<br>usuários<br>operacionais do<br>sistema. Realizam<br>os atendimentos de<br>forma presencial.|Cadastrar tutores,<br>Pets e vacinas;<br>realizar e gerenciar<br>agendamentos;<br>registrar<br>aplicações;<br>consultar históricos;<br>acompanhar<br>notificações e<br>próximas doses.|Usuários do<br>Sistema|
|Tutores|Responsáveis<br>pelos animais<br>atendidos pela<br>empresa.|Fornecer aos<br>médicos<br>veterinários as<br>informações<br>necessárias sobre<br>seus Pets e receber<br>orientações<br>relacionadas à<br>vacinação durante<br>o atendimento.|Clientes da<br>Empresa|



### **8. Benefícios Esperados** 

Do lado dos tutores – Os tutores poderão contar com um acompanhamento mais organizado das vacinações de seus Pets durante os atendimentos presenciais, uma vez 

que os médicos veterinários terão acesso ao histórico e às próximas doses registradas no sistema. 

Do lado dos médicos veterinários e da empresa– O sistema proporcionará maior organização dos agendamentos e registros de vacinação, facilitará a consulta ao histórico dos Pets e auxiliará no acompanhamento das próximas doses por meio de notificações e lembretes, reduzindo falhas administrativas e esquecimentos. 

### **9. Técnicas de levantamento** 

As técnicas utilizadas neste estudo foram: 

- A análise de softwares e empresas já existentes no mercado. 

- Diagrama de Casos de Uso. 

- Prototipação das telas do sistema. 

- Brainstorming com os stakeholders. 

- Pesquisa e análise do mercado Pet, com foco em vacinação e acompanhamento de imunização. 

