import { getDataWithConfig } from './api';

export function downloadAuthorizedFile(url, fileName = 'downloaded-file.ext') {
    getDataWithConfig(url, {
        responseType: 'blob',
    })
        .then(response => {
            const downloadUrl = URL.createObjectURL(new Blob([response]));
            const link = document.createElement('a');
            link.href = downloadUrl;
            link.download = fileName;
            document.body.appendChild(link);
            link.click();
            link.remove();
            URL.revokeObjectURL(downloadUrl);
        })
        .catch(error => {
            console.error('File download failed:', error);
        });
}
