program teste4 is
declare
    pontuacao, pontuacaoMaxina, disponibilidade: int;
    pontuacaoMinima: char
init
    pontuacaoMinima = 50;
    pontuacaoMaxima = 100;
    out >> "Pontuacao Candidato:";
    in << pontuacao;
    out >> "Disponibilidade Candidato: ";
    in << disponibilidade;

    while (pontuacao>0 && (pontuação<=pontuacaoMaxima)) do
        if ((pontuação > pontuacaoMinima) && (disponibilidade==1)) then
            out >> "Candidato aprovado"
        else
            out >>"Candidato reprovado"
        end;
        out>>"Pontuacao Candidato: ";
        in << pontuacao;
        out>>"Disponibilidade Candidato: ";
        in << disponibilidade
    end
end