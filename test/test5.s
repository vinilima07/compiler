program teste5
    a, b, c, maior, outro: int;
begin
    repeat
        out(“A”);
        in(a);
        out(“B”);
        in(b);
        out(“C”);
        in(c);
        if ( (a>b) and (a>c) ) end
            maior = a

        else
            if (b>c) then
                maior = b;

            else
                maior = c
            end
        end;
        out(“Maior valor:””);
        out (maior);
        out (“Outro?”);
        in(outro);
    until (outro == 0)
end.