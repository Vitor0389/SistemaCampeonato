import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useParams } from 'react-router-dom';

export default function DetalhesCampeonatoPage() {
    const { id } = useParams();
    const [fases, setFases] = useState([]);
    const [error, setError] = useState('');

    useEffect(() => {
        const fetchDetalhes = async () => {
            try {
                const token = localStorage.getItem('token');
                if (!token) {
                    setError('Usuário não autenticado');
                    return;
                }
                const response = await axios.get(`http://localhost:8080/api/v1/campeonatos/${id}`, {
                    headers: { Authorization: `Bearer ${token}` }
                });
                setFases(response.data);
            } catch (err) {
                setError('Erro ao buscar detalhes do campeonato');
            }
        };

        fetchDetalhes();
    }, [id]);

    return (
        <div style={{ maxWidth: '800px', margin: 'auto' }}>
            <h2>Detalhes do Campeonato</h2>
            {error && <p style={{ color: 'red' }}>{error}</p>}
            {!error && fases.length === 0 && <p>Carregando detalhes...</p>}

            {fases.map((fase, idx) => (
                <div key={idx} style={{ marginBottom: '30px', border: '1px solid #ccc', padding: '15px', borderRadius: '8px' }}>
                    <h3>{fase.name}</h3>
                    <h4>Partidas:</h4>
                    <ul>
                        {fase.partidas.map(partida => (
                            <li key={partida.uuid} style={{ marginBottom: '5px' }}>
                                {partida.teamA?.name || '---'} vs {partida.teamB?.name || '---'} —
                                <strong> Vencedor: </strong> {partida.winner ? partida.winner.name : 'Não definido'}
                            </li>
                        ))}
                    </ul>

                    <h4>Vencedores da fase:</h4>
                    <ul>
                        {fase.vencedores.length === 0 && <li>Nenhum vencedor registrado ainda</li>}
                        {fase.vencedores.map(time => (
                            <li key={time.id}>{time.name}</li>
                        ))}
                    </ul>
                </div>
            ))}
        </div>
    );
}
