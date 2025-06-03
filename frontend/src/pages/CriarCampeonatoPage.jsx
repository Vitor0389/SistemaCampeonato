import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

export default function CriarCampeonatoPage() {
    const [nome, setNome] = useState('');
    const [times, setTimes] = useState([]);
    const [selecionados, setSelecionados] = useState(new Set());
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        // Carrega lista de times do backend
        const fetchTimes = async () => {
            try {
                const token = localStorage.getItem('token');
                if (!token) {
                    setError('Usuário não autenticado');
                    return;
                }
                const response = await axios.get('http://localhost:8080/api/v1/teams', {
                    headers: { Authorization: `Bearer ${token}` }
                });
                setTimes(response.data);
            } catch {
                setError('Erro ao carregar times');
            }
        };

        fetchTimes();
    }, []);

    const toggleSelecionado = (id) => {
        setSelecionados(prev => {
            const novo = new Set(prev);
            if (novo.has(id)) {
                novo.delete(id);
            } else {
                novo.add(id);
            }
            return novo;
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setSuccess('');

        if (!nome.trim()) {
            setError('Nome do campeonato é obrigatório');
            return;
        }

        if (selecionados.size === 0) {
            setError('Selecione ao menos um time');
            return;
        }

        // Construir objeto times para enviar (array de TeamDTOs)
        const teamsDTO = times
            .filter(time => selecionados.has(time.id))
            .map(time => ({ id: time.id, name: time.name }));

        try {
            const token = localStorage.getItem('token');
            const response = await axios.post(
                'http://localhost:8080/api/v1/campeonatos',
                { name: nome, teams: teamsDTO },
                { headers: { Authorization: `Bearer ${token}` } }
            );
            setSuccess('Campeonato criado com sucesso!');
            setNome('');
            setSelecionados(new Set());
            // Opcional: redirecionar para lista ou detalhes
            // navigate('/campeonatos');
        } catch (err) {
            setError('Erro ao criar campeonato');
        }
    };

    return (
        <div style={{ maxWidth: 600, margin: 'auto', padding: 20 }}>
            <h2>Criar Campeonato</h2>
            {error && <p style={{ color: 'red' }}>{error}</p>}
            {success && <p style={{ color: 'green' }}>{success}</p>}
            <form onSubmit={handleSubmit}>
                <div style={{ marginBottom: 10 }}>
                    <label>
                        Nome do Campeonato:<br />
                        <input
                            type="text"
                            value={nome}
                            onChange={e => setNome(e.target.value)}
                            style={{ width: '100%', padding: 8, fontSize: 16 }}
                        />
                    </label>
                </div>
                <div>
                    <p>Selecione os times:</p>
                    {times.length === 0 && <p>Nenhum time disponível.</p>}
                    <ul style={{ listStyle: 'none', padding: 0, maxHeight: 200, overflowY: 'auto', border: '1px solid #ccc' }}>
                        {times.map(time => (
                            <li key={time.id} style={{ marginBottom: 5 }}>
                                <label>
                                    <input
                                        type="checkbox"
                                        checked={selecionados.has(time.id)}
                                        onChange={() => toggleSelecionado(time.id)}
                                    />
                                    {' '}{time.name}
                                </label>
                            </li>
                        ))}
                    </ul>
                </div>
                <button type="submit" style={{ marginTop: 15, padding: '10px 20px', fontSize: 16 }}>
                    Criar
                </button>
            </form>
        </div>
    );
}