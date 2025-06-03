import React from 'react';
import { useNavigate } from 'react-router-dom';
import './MainPage.css';

export default function MainPage() {
    const navigate = useNavigate();

    const handleLogout = () => {
        localStorage.removeItem('token');
        navigate('/login');
    };

    return (
        <div className="container">
            <h2>Menu Principal</h2>
            <button className="btn" onClick={() => navigate('/criar-campeonato')}>Criar Campeonato</button>
            <button className="btn" onClick={() => navigate('/campeonatos')}>Buscar Campeonatos</button>
            <button className="btn" onClick={() => navigate('/deletar')}>Deletar Campeonato</button>
            <button className="btn" onClick={() => navigate('/registrar-resultado')}>Registrar Resultado de Partida</button>
            <button className="btn logout" onClick={handleLogout}>Sair</button>
        </div>
    );
}
