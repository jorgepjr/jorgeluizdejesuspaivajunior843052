INSERT INTO album (nome)
SELECT unnest(ARRAY[
    'Slippery When Wet',
    'Music Box',
    'The Eminem Show',
    'Hybrid Theory',
    'Come Away with Me',
    'Unplugged',
    'True Blue',
    'Legend',
    'Tapestry',
    'No Jacket Required',
    'O Papa é Pop',
    'Ventura',
    'Bloco do Eu Sozinho',
    'Selvagem?',
    'Maior Abandonado',
    'Purple Rain',
    'The Joshua Tree',
    'Thriller',
    'Back in Black',
    'Nevermind'
])
FROM generate_series(1,1)
ORDER BY random()
LIMIT 20;
