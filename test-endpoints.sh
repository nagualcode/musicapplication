#!/bin/bash

# URL base da aplicação
BASE_URL="http://localhost:8080/api/music"

# Função para testar o endpoint GET /api/music
function test_get_all_music {
    echo "Testando GET /api/music..."
    curl -s -X GET "$BASE_URL" -H "Accept: application/json" | jq .
    echo ""
}

# Função para testar o endpoint POST /api/music
function test_create_music {
    echo "Testando POST /api/music..."
    RESPONSE=$(curl -s -X POST "$BASE_URL" \
    -H "Content-Type: application/json" \
    -d '{"title":"Song A","artist":"Artist A"}')
    
    echo "$RESPONSE" | jq .
    echo ""

    # Extrai o ID da música criada
    MUSIC_ID=$(echo "$RESPONSE" | jq -r .id)
}

# Função para testar o endpoint GET /api/music/{id}
function test_get_music_by_id {
    echo "Testando GET /api/music/$MUSIC_ID..."
    curl -s -X GET "$BASE_URL/$MUSIC_ID" -H "Accept: application/json" | jq .
    echo ""
}

# Função para testar o endpoint PUT /api/music/{id}
function test_update_music {
    echo "Testando PUT /api/music/$MUSIC_ID..."
    curl -s -X PUT "$BASE_URL/$MUSIC_ID" \
    -H "Content-Type: application/json" \
    -d '{"title":"Updated Song","artist":"Updated Artist"}' | jq .
    echo ""
}

# Função para testar o endpoint DELETE /api/music/{id}
function test_delete_music {
    echo "Testando DELETE /api/music/$MUSIC_ID..."
    curl -s -X DELETE "$BASE_URL/$MUSIC_ID"
    echo ""
}

# Executa os testes
test_get_all_music
test_create_music
test_get_music_by_id
test_update_music
test_get_music_by_id
test_delete_music

# Verifica se a música foi deletada
echo "Verificando se a música foi deletada..."
curl -s -X GET "$BASE_URL/$MUSIC_ID" -H "Accept: application/json" | jq .
