program teste5 is
declare
    a, b, c, maior, outro: int
init
    repeat
        out >> "A";
        in << a;
        out >> "B";
        in << b;
        out >> "C";
        in << c;
        if ( (a>b) && (a>c) ) then
            maior = a

        else
            if (b>c) then
                maior = b

            else
                maior = c
            end
        end;
        out >> "Maior valor:";
        out >> maior;
        out >> "Outro?";
        in << outro
    until (outro == 0)
end
