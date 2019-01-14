function [] = RSSIauto(local)
    if exist('t','var')
        fclose(t)
        delete(t)
        clear t 
        pause(1)
    end
    fileID = fopen('valores.txt','a+');
    fmt = '%d, %d, %d, %d, %d, %d, %d, %d, '+local+'\n'
    t = tcpip('192.168.0.117',8090)
    fopen(t)
    i = 0;
    j = 0;
    while i < 5
        fwrite(t,i);
        pause(1);
        t.BytesAvailable
        if t.BytesAvailable == 0
            j = j+1;
            pause(1)
        else
            if t.BytesAvailable ~= 0 && t.BytesAvailable == 8
                j = 0;
                i = i+1;
                resp = fread(t,t.BytesAvailable,'int8')
                fprintf(fileID, fmt, resp);
            else
              resp = fread(t,t.BytesAvailable,'int8');
            end 
            pause(3);
        end
    end

    fclose(t)
    delete(t)
    clear t
    fclose(fileID)
end

