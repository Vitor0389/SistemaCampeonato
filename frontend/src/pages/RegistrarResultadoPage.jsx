import React, { useEffect, useState } from 'react';
import axios from 'axios';

export default function RegistrarResultadoPage() {
    const [campeonatos, setCampeonatos] = useState([]);
    const [selectedCampeonato, setSelectedCampeonato] = useState(null);
    const [fases, setFases] = useState([]);
    const [selectedFase, setSelectedFase] = useState(null);
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    const token = localStorage.getItem('token');

    useEffect(() => {
        const fetchCampeonatos = async () => {
            try {
                setError('');
                const response = await axios.get('http://localhost:8080/api/v1/campeonatos', {
                    headers: { Authorization: `Bearer ${token}` }
                });
                setCampeonatos(response.data);
            } catch (err) {
                setError('Erro ao buscar campeonatos');
            }
        };
        fetchCampeonatos();
    }, [token]);

    useEffect(() => {
        const fetchFases = async () => {
            if (!selectedCampeonato) return;
            try {
                setError('');
                const response = await axios.get(`http://localhost:8080/api/v1/campeonatos/${selectedCampeonato.id}`, {
                    headers: { Authorization: `Bearer ${token}` }
                });
                setFases(response.data);
                setSelectedFase(null);
            } catch (err) {
                setError('Erro ao buscar fases');
            }
        };
        fetchFases();
    }, [selectedCampeonato, token]);

    const registrarResultado = async (partidaId, vencedor) => {
        try {
            setError('');
            setLoading(true);
            await axios.patch(
                `http://localhost:8080/api/v1/campeonatos/${selectedCampeonato.id}/resultado/partida/${partidaId}`,
                vencedor,
                { headers: { Authorization: `Bearer ${token}` } }
            );
            // Recarrega fases
            const response = await axios.get(`http://localhost:8080/api/v1/campeonatos/${selectedCampeonato.id}`, {
                headers: { Authorization: `Bearer ${token}` }
            });
            setFases(response.data);
            // Se estava com fase selecionada, recarrega a mesma
            const novaFase = response.data.find(f => f.name === selectedFase?.name);
            setSelectedFase(novaFase || null);
            setLoading(false);
        } catch (err) {
            setLoading(false);
        }
    };

    return (
        <div style={{ maxWidth: '600px', margin: 'auto' }}>
            <h2>Registrar Resultado de Partida</h2>
            {error && <p style={{ color: 'red' }}>{error}</p>}

            <div>
                <h3>Selecione o Campeonato</h3>
                <select
                    value={selectedCampeonato?.id || ''}
                    onChange={(e) =>
                        setSelectedCampeonato(
                            campeonatos.find(c => c.id === e.target.value) || null
                        )
                    }
                >
                    <option value="" disabled>-- Escolha um campeonato --</option>
                    {campeonatos.map(c => (
                        <option key={c.id} value={c.id}>{c.name}</option>
                    ))}
                </select>
            </div>

            {selectedCampeonato && (
                <div>
                    <h3>Fases</h3>
                    <ul>
                        {fases.map(fase => (
                            <li key={fase.name}>
                                <button onClick={() => setSelectedFase(fase)}>{fase.name || 'Fase'}</button>
                            </li>
                        ))}
                    </ul>
                </div>
            )}

            {selectedFase && (
                <div>
                    <h3>Partidas da fase {selectedFase.name || ''}</h3>
                    {Array.isArray(selectedFase.partidas) && selectedFase.partidas.length > 0 ? (
                        <ul>
                            {selectedFase.partidas.map(partida => (
                                <li key={partida.uuid} style={{ marginBottom: '10px' }}>
                                    <div>
                                        <span>{partida.teamA.name} vs {partida.teamB.name} - </span>
                                        {partida.winner ? (
                                            <strong style={{ color: 'green', marginLeft: '10px' }}>
                                                Vencedor: {partida.winner.name}
                                            </strong>
                                        ) : (
                                            <>
                                                <span>Escolha o vencedor: </span>
                                                {[partida.teamA, partida.teamB].map(time => (
                                                    <button
                                                        key={time.id}
                                                        onClick={() => registrarResultado(partida.uuid, time)}
                                                        disabled={loading}
                                                        style={{ marginLeft: '5px' }}
                                                    >
                                                        {time.name}
                                                    </button>
                                                ))}
                                            </>
                                        )}
                                    </div>
                                </li>
                            ))}
                        </ul>
                    ) : (
                        <p>Nenhuma partida nesta fase.</p>
                    )}
                </div>
            )}
        </div>
    );
}
