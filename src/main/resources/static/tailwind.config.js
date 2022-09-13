/** @type {import('tailwindcss').Config} */

/* function formatRGB(variable){
  return variable.slice(0,-1).substring(2);
} */

function withOpacity(variable){
  return ({opacityValue}) => {
    if (opacityValue != undefined){
      return `rgba(var(${variable}),${opacityValue})`;
    }
    return `rgb(var(${variable}))`;
  }
}

module.exports = {
  content: [
    './styles/*.css',
    './web/scripts/*.js',
    './web/*.html',
    './*.html'
  ],
  theme: {
    extend: {
      textColor: {
        skin: {
          base: withOpacity('--color-text-base'),
          muted: withOpacity('--color-text-muted'),
          inverted: withOpacity('--color-text-inverted')
        }
      },
      backgroundColor: {
        skin: {
          fill: withOpacity('--color-fill'),
          'button-accent': withOpacity('--color-button-accent'),
          'button-hover-accent': withOpacity('--color-button-hover'),
          'button-muted': withOpacity('--color-button-muted'),
          'normal': withOpacity('--bg-normal')
        }
      },
      gradientColorStops: {
        skin: {
          fill: withOpacity('--color-fill') ,
          'button-accent': withOpacity('--color-button-accent') ,
          'button-hover-accent': withOpacity('--color-button-hover') ,
          'button-muted': withOpacity('--color-button-muted') ,
          'normal': withOpacity('--bg-normal') 
        }
      },
      aspectRatio: {
        'card': '8.6/5.4',
      },
      fontFamily:{
        'bahnschrift' : ['Bahnschrift','sans-serif'],
        'notosans' : ['Notosans', 'sans-serif']
      }

    },
  },
  plugins: [],
}


/* npx tailwindcss -i ./styles/style.css -o ./styles/tailwind.css --watch */