const IMG_BASE_URL = 'https://image.tmdb.org/t/p/w500';
const BACKDROP_BASE_URL = 'https://image.tmdb.org/t/p/original';

async function fetchMovies(type) {
    try {
        const response = await fetch(`/api/movies/${type}`);
        const data = await response.json();
        return data.results;
    } catch (error) {
        console.error(`Error fetching ${type} movies:`, error);
        return [];
    }
}

async function searchMovies(query) {
    try {
        const response = await fetch(`/api/search?query=${encodeURIComponent(query)}`);
        const data = await response.json();
        return data.results;
    } catch (error) {
        console.error('Error searching movies:', error);
        return [];
    }
}

window.playTrailer = function(key) {
    const container = document.getElementById('trailerContainer');
    if (container) {
        container.innerHTML = `<iframe width="100%" height="315" src="https://www.youtube.com/embed/${key}?autoplay=1" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" allowfullscreen style="border-radius: 10px; margin-top: 1rem;"></iframe>`;
        container.style.display = 'block';
    }
};

function createMovieCard(movie) {
    const card = document.createElement('div');
    card.className = 'movie-card';
    card.innerHTML = `
        <img src="${IMG_BASE_URL}${movie.poster_path}" alt="${movie.title}" loading="lazy">
        <div class="movie-info">
            <h3>${movie.title}</h3>
            <div class="movie-meta">
                <span>⭐ ${movie.vote_average.toFixed(1)}</span>
                <span>${movie.release_date ? movie.release_date.split('-')[0] : 'N/A'}</span>
            </div>
        </div>
    `;
    card.onclick = () => showMovieDetails(movie.id);
    return card;
}

async function showMovieDetails(id) {
    const modal = document.getElementById('movieModal');
    const detailsContainer = document.getElementById('modalDetails');
    modal.style.display = 'flex';
    detailsContainer.innerHTML = '<p>Loading...</p>';

    try {
        const response = await fetch(`/api/movie/${id}`);
        const movie = await response.json();

        detailsContainer.innerHTML = `
            <div class="modal-body" style="background: linear-gradient(rgba(0,0,0,0.7), rgba(0,0,0,0.7)), url(${BACKDROP_BASE_URL}${movie.backdrop_path}) center/cover; padding: 2rem; border-radius: 15px;">
                <div style="display: flex; gap: 2rem; flex-wrap: wrap;">
                    <img src="${IMG_BASE_URL}${movie.poster_path}" style="width: 250px; border-radius: 10px; box-shadow: 0 0 20px rgba(0,0,0,0.5);">
                    <div style="flex: 1; min-width: 300px;">
                        <h2 style="font-size: 2.5rem; margin-bottom: 1rem;">${movie.title}</h2>
                        <p style="color: var(--neon-cyan); font-weight: 600; margin-bottom: 1rem;">${movie.genres.map(g => g.name).join(', ')}</p>
                        <p style="font-size: 1.1rem; line-height: 1.6; margin-bottom: 1.5rem;">${movie.overview}</p>
                        <div style="display: flex; gap: 2rem; margin-bottom: 2rem;">
                            <div>
                                <p style="color: var(--text-dim);">Release Date</p>
                                <p>${movie.release_date}</p>
                            </div>
                            <div>
                                <p style="color: var(--text-dim);">Rating</p>
                                <p>⭐ ${movie.vote_average}</p>
                            </div>
                            <div>
                                <p style="color: var(--text-dim);">Runtime</p>
                                <p>${movie.runtime} min</p>
                            </div>
                        </div>
                        ${(() => {
                            const trailer = movie.videos && movie.videos.results ? movie.videos.results.find(v => v.type === 'Trailer' && v.site === 'YouTube') : null;
                            if (trailer) {
                                return `<button class="btn btn-primary" onclick="playTrailer('${trailer.key}')">Watch Trailer</button>`;
                            } else {
                                return `<button class="btn btn-secondary" disabled>No Trailer</button>`;
                            }
                        })()}
                        <div id="trailerContainer" style="display: none;"></div>
                    </div>
                </div>
            </div>
        `;
    } catch (error) {
        detailsContainer.innerHTML = '<p>Error loading movie details.</p>';
    }
}

async function init() {
    const upcomingGrid = document.getElementById('upcomingMovies');
    const popularGrid = document.getElementById('popularMovies');
    const topRatedGrid = document.getElementById('topRatedMovies');

    const [upcoming, popular, topRated] = await Promise.all([
        fetchMovies('upcoming'),
        fetchMovies('popular'),
        fetchMovies('top_rated')
    ]);

    upcoming.slice(0, 10).forEach(movie => {
        upcomingGrid.appendChild(createMovieCard(movie));
    });

    popular.slice(0, 10).forEach(movie => {
        popularGrid.appendChild(createMovieCard(movie));
    });

    topRated.slice(0, 10).forEach(movie => {
        topRatedGrid.appendChild(createMovieCard(movie));
    });

    // Close modal
    document.querySelector('.close-btn').onclick = () => {
        document.getElementById('movieModal').style.display = 'none';
        const trailerContainer = document.getElementById('trailerContainer');
        if (trailerContainer) {
            trailerContainer.innerHTML = ''; // Stop video playback when closing
        }
    };

    window.onclick = (event) => {
        const modal = document.getElementById('movieModal');
        if (event.target == modal) {
            modal.style.display = 'none';
            const trailerContainer = document.getElementById('trailerContainer');
            if (trailerContainer) {
                trailerContainer.innerHTML = ''; // Stop video playback when closing
            }
        }
    };
    
    // Search Functionality
    const searchBtn = document.getElementById('searchBtn');
    const searchInput = document.getElementById('searchInput');
    const clearSearchBtn = document.getElementById('clearSearchBtn');
    const searchResultsSection = document.getElementById('searchResultsSection');
    const heroSection = document.getElementById('hero');
    const otherSections = document.querySelectorAll('.movie-section:not(#searchResultsSection)');
    const searchResultsDiv = document.getElementById('searchResults');

    const performSearch = async () => {
        const query = searchInput.value.trim();
        if (!query) return;

        heroSection.style.display = 'none';
        otherSections.forEach(sec => sec.style.display = 'none');
        searchResultsSection.style.display = 'block';
        
        searchResultsDiv.innerHTML = '<p>Loading...</p>';
        
        const results = await searchMovies(query);
        searchResultsDiv.innerHTML = '';
        
        if (results.length === 0) {
            searchResultsDiv.innerHTML = '<p>No movies found.</p>';
        } else {
            results.forEach(movie => {
                if (movie.poster_path) {
                    searchResultsDiv.appendChild(createMovieCard(movie));
                }
            });
        }
    };

    searchBtn.onclick = performSearch;

    searchInput.addEventListener('keypress', (e) => {
        if (e.key === 'Enter') {
            performSearch();
        }
    });

    clearSearchBtn.onclick = (e) => {
        e.preventDefault();
        searchInput.value = '';
        searchResultsSection.style.display = 'none';
        heroSection.style.display = 'flex';
        otherSections.forEach(sec => sec.style.display = 'block');
    };
}

init();
