#!/bin/bash

# Caminho correto da MainActivity no AndroGoat
MAIN_ACTIVITY="app/src/main/java/owasp/sat/agoat/MainActivity.kt"

# Verifica se o arquivo existe antes de modificar
if [ -f "$MAIN_ACTIVITY" ]; then
    echo "Modificando MainActivity.kt para incluir RASP..."

    # Adiciona importação do Android RASP
    sed -i '/import android.os.Bundle/a import securevale.rasp.RASP' $MAIN_ACTIVITY

    # Adiciona código dentro do onCreate para verificar root e emulador
    sed -i '/super.onCreate(savedInstanceState)/a \
        \n        // Inicializa o RASP\n        val rasp = RASP(this)\n\
        if (rasp.isRooted()) {\n\
            Toast.makeText(this, "Dispositivo rooteado! Encerrando o app.", Toast.LENGTH_LONG).show()\n\
            finish()\n\
        }\n\
        if (rasp.isEmulator()) {\n\
            Toast.makeText(this, "Emulador detectado! Encerrando o app.", Toast.LENGTH_LONG).show()\n\
            finish()\n\
        }' $MAIN_ACTIVITY

    echo "MainActivity.kt atualizado com sucesso!"
else
    echo "Erro: MainActivity.kt não encontrado!"
    exit 1
fi
