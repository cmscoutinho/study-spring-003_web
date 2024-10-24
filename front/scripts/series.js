import getDados from './getDados.js';

const params = new URLSearchParams(window.location.search);
const serieId = params.get('id');
const listaTemporadas = document.getElementById('temporadas-select');
const fichaSerie = document.getElementById('temporadas-episodios');
const fichaDescricao = document.getElementById('ficha-descricao');

// Função para carregar temporadas
function carregarTemporadas() {
  getDados(`/series/${serieId}/seasons/all`)
    .then((data) => {
      const temporadasUnicas = [
        ...new Set(data.map((temporada) => temporada.season)),
      ];
      listaTemporadas.innerHTML = ''; // Limpa as opções existentes

      const optionDefault = document.createElement('option');
      optionDefault.value = '';
      optionDefault.textContent = 'Selecione a temporada';
      listaTemporadas.appendChild(optionDefault);

      temporadasUnicas.forEach((temporada) => {
        const option = document.createElement('option');
        option.value = temporada;
        option.textContent = temporada;
        listaTemporadas.appendChild(option);
      });

      const optionTodos = document.createElement('option');
      optionTodos.value = 'all';
      optionTodos.textContent = 'Todas as temporadas';
      listaTemporadas.appendChild(optionTodos);

      const optionTop5 = document.createElement('option');
      optionTop5.value = 'top5';
      optionTop5.textContent = 'Top 5 episódios';
      listaTemporadas.appendChild(optionTop5);
    })
    .catch((error) => {
      console.error('Erro ao obter temporadas:', error);
    });
}

// Função para carregar episódios de uma temporada
function carregarEpisodios() {
  getDados(`/series/${serieId}/seasons/${listaTemporadas.value}`)
    .then((data) => {
      const temporadasUnicas = [
        ...new Set(data.map((temporada) => temporada.season)),
      ];
      fichaSerie.innerHTML = '';
      temporadasUnicas.forEach((temporada) => {
        const ul = document.createElement('ul');
        ul.className = 'episodios-lista';

        const episodiosTemporadaAtual = data.filter(
          (series) => series.season === temporada
        );

        const listaHTML = episodiosTemporadaAtual
          .map(
            (serie) => `
                    <li>
                        ${serie.episodeIdx} - ${serie.title}
                    </li>
                `
          )
          .join('');
        ul.innerHTML = listaHTML;

        const paragrafo = document.createElement('p');
        const linha = document.createElement('br');
        paragrafo.textContent = `Temporada ${temporada}`;
        fichaSerie.appendChild(paragrafo);
        fichaSerie.appendChild(linha);
        fichaSerie.appendChild(ul);
      });
    })
    .catch((error) => {
      console.error('Erro ao obter episódios:', error);
    });
}

function carregarTopEpisodios() {
  getDados(`/series/${serieId}/temporadas/top`)
    .then((data) => {
      fichaSerie.innerHTML = '';
      const ul = document.createElement('ul');
      ul.className = 'episodios-lista';

      const listaHTML = data
        .map(
          (serie) => `
                <li>
                    Episódio ${serie.numeroEpisodio} - Temporada ${serie.temporada} - ${serie.titulo}
                </li>
            `
        )
        .join('');
      ul.innerHTML = listaHTML;

      const paragrafo = document.createElement('p');
      const linha = document.createElement('br');
      fichaSerie.appendChild(paragrafo);
      fichaSerie.appendChild(linha);
      fichaSerie.appendChild(ul);
    })
    .catch((error) => {
      console.error('Erro ao obter episódios:', error);
    });
}

// Função para carregar informações da série
function carregarInfoSerie() {
  getDados(`/series/${serieId}`)
    .then((data) => {
      fichaDescricao.innerHTML = `
                <img src="${data.poster}" alt="${data.title}" />
                <div>
                    <h2>${data.title}</h2>
                    <div class="descricao-texto">
                        <p><b>Média de avaliações:</b> ${data.rating}</p>
                        <p>${data.plot}</p>
                        <p><b>Estrelando:</b> ${data.actors}</p>
                    </div>
                </div>
            `;
    })
    .catch((error) => {
      console.error('Erro ao obter informações da série:', error);
    });
}

// Adiciona ouvinte de evento para o elemento select
listaTemporadas.addEventListener('change', carregarEpisodios);

// Carrega as informações da série e as temporadas quando a página carrega
carregarInfoSerie();
carregarTemporadas();
